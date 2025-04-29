package com.harris.cinnato.outputs;

import java.io.File;
import java.io.OutputStream;
import java.util.concurrent.PriorityBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.typesafe.config.Config;

public class QueueOutput extends Output {
    private PriorityBlockingQueue<String> atl_queue;
    private PriorityBlockingQueue<String> clt_queue;
    
    private final long RUNTIME_HOURS = 7; // Time in hours
    private final long RUNTIME = RUNTIME_HOURS * 60 * 60 * 1000;
    private final int EXIT_CONDITION = 2; // Number of threads to finish

    private static int exitState = 0; 

    // private static final String[] ATL_COORDS = {"33.636667","-84.428056"}; // Atlanta
    // private static final String[] CLT_COORDS = {"35.213890","-80.943054"}; // Charlotte

    public QueueOutput(Config config) {
        super(config);
        atl_queue = new PriorityBlockingQueue<>();
        clt_queue = new PriorityBlockingQueue<>();
        Thread thread1 = new Thread(() -> { 
            printItemsAtlanta();
        });
        Thread thread2 = new Thread(() -> {
            printItemsCharlotte();
        });
        thread1.start();
        thread2.start();
        // When both threads are complete, exit the program

    }

    @Override
    public void output(String message, String header) {
        // Determine if the message is for Atlanta or Charlotte
        if (message.contains("ATL") || header.contains("ATL")) {
            addToQueueAtlanta(message, header);
        } else if (message.contains("CLT") || header.contains("CLT")) {
            addToQueueCharlotte(message, header);
        } else {
            System.out.println("DEBUG: No ATL or CLT in message");
        }
        
    }

    private void addToQueueAtlanta(String message, String header) {
        if (this.config.getBoolean("headers")) {
            atl_queue.add(header + this.convert(message));
        } else {
            atl_queue.add(this.convert(message));
        }
    }
    
    private void addToQueueCharlotte(String message, String header) {
        if (this.config.getBoolean("headers")) {
            clt_queue.add(header + this.convert(message));
        } else {
            clt_queue.add(this.convert(message));
        }
    }

    private void printItemsAtlanta() {
        String location = "atl";
        printItems(location);
    }

    private void printItemsCharlotte() {
        String location = "clt";
        printItems(location);
    }

    private void printItems(String location) {
        String fileName = null;
        if(location.equals("atl"))
            fileName = "./data/flight_data_atl.json";
        else if(location.equals("clt"))
            fileName = "./data/flight_data_clt.json";
        else
            System.out.println("DEBUG: No location found");
        // Start the file.
        fileFormat(false, fileName);
        long startTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - startTime < RUNTIME) {
            String topQueue = null;
            try {
                topQueue = getTopOfQueue(location);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
            if(topQueue == null) {
                System.out.println("DEBUG: NULL QUEUE");
                System.exit(1);
            }

            // Convert top into a JsonObject from Gson
            Gson gson = new Gson();
            JsonObject top = gson.fromJson(topQueue, JsonObject.class);
            if(top.has("ns2:asdexMsg") == false) {
                System.out.println("DEBUG: No ns2:asdexMsg");
                continue;
            }

            JsonObject data = top.getAsJsonObject("ns2:asdexMsg");

            writePositionReport(data, fileName);
        }
        
        fileFormat(true, fileName);
        exitState++;
        if(exitState == EXIT_CONDITION) {
            System.exit(0);
        }
        Thread.yield();
    }

    private void fileFormat(boolean end, String fileName) {
		try {
			File file = new File(fileName);
			if (end) {
                OutputStream os = new java.io.FileOutputStream(file, true);
				os.write("]}".getBytes());
                os.close();
			} else {
                OutputStream os = new java.io.FileOutputStream(file, false);
				os.write("{\"data\": [\n".getBytes());
                os.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private static void printJsonObjectToFile(JsonObject object, String fileName) {
		try {
			File file = new File(fileName);
			OutputStream os = new java.io.FileOutputStream(file, true);
			os.write("\t".getBytes());
			os.write(object.toString().getBytes());
			os.write(",\n".getBytes());
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private String getTopOfQueue(String location) throws InterruptedException {
        if(location.equals("atl")) {
            return atl_queue.take();
        } else if(location.equals("clt")) {
            return clt_queue.take();
        }
        return null;
    }

    private void writePositionReport(JsonObject data, String fileName) {
        if(data.has("positionReport") && data.get("positionReport").isJsonArray()) {
            JsonArray positionReport = data.getAsJsonArray("positionReport");
            for(Object obj : positionReport) {
                JsonObject jsonObj = (JsonObject) obj;
                JsonObject toPrint = new JsonObject();

                toPrint.add("stid", jsonObj.get("stid"));
                toPrint.add("seqNum", jsonObj.get("seqNum"));
                toPrint.add("latitude", jsonObj.getAsJsonObject("position").get("latitude"));
                toPrint.add("longitude", jsonObj.getAsJsonObject("position").get("longitude"));
                toPrint.add("time", jsonObj.get("time"));

                printJsonObjectToFile(toPrint, fileName);
            }
        } 
        else if(data.has("positionReport") && data.get("positionReport").isJsonObject()) {
            JsonObject positionReport = data.getAsJsonObject("positionReport");
            JsonObject toPrint = new JsonObject();

            toPrint.add("stid", positionReport.get("stid"));
            toPrint.add("seqNum", positionReport.get("seqNum"));
            toPrint.add("latitude", positionReport.getAsJsonObject("position").get("latitude"));
            toPrint.add("longitude", positionReport.getAsJsonObject("position").get("longitude"));
            toPrint.add("time", positionReport.get("time"));

            printJsonObjectToFile(toPrint, fileName);
        }
        else {
            return;
        }
    }
}
