import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;


public class risingCity {

    private RedBlackTree redBlackTree;
    private MinHeap minHeap;

    public risingCity() {
        redBlackTree = new RedBlackTree();
        minHeap = new MinHeap();
    }

    private void insert(int buildingNum, int totalTime) {
        if (redBlackTree.contains(buildingNum)) {
            System.out.printf("buildingNum %d is a duplicate\n", buildingNum);
            throw new IllegalArgumentException();
        }
        Record record = new Record(buildingNum, totalTime);
        redBlackTree.insert(record);
        minHeap.insert(record);
    }

    private void print(int buildingNum) {
        Record record = redBlackTree.get(buildingNum);
        if (record == null) {
            System.out.println("(0,0,0)");
        } else {
            System.out.println(record);
        }
    }

    private void print(int buildingNum1, int buildingNum2) {
        List<Record> records = redBlackTree.getByInterval(buildingNum1, buildingNum2);
        if (records.size() == 0) {
            System.out.println("(0,0,0)");
            return;
        }
        for (int i = 0; i < records.size(); i++) {
            System.out.print(records.get(i));
            if (i < records.size() - 1) {
                System.out.print(",");
            }
        }
        System.out.println("");
    }

    private void execCmd(String cmdAndParams) {
        // remove the last brackets
        cmdAndParams = cmdAndParams.substring(0, cmdAndParams.length() - 1);
        // parse command
        String cmd = cmdAndParams.split("\\(")[0].trim();
        // parse parameters
        String[] params = cmdAndParams.split("\\(")[1].split(",");

        if (cmd.equals("Insert")) {
            int buildingNum = Integer.parseInt(params[0]);
            int totalTime = Integer.parseInt(params[1]);
            insert(buildingNum, totalTime);
        } else if (params.length == 1) {
            int buildingNum = Integer.parseInt(params[0]);
            print(buildingNum);
        } else if (params.length == 2) {
            int buildingNum1 = Integer.parseInt(params[0]);
            int buildingNum2 = Integer.parseInt(params[1]);
            print(buildingNum1, buildingNum2);
        }

    }


    private void run(String inputFile) throws FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream("output_file.txt")));
        Scanner inpnut = new Scanner(new FileInputStream(inputFile));
        long timeCounter = 0;
        int time = -1;
        int workDaysInBuilding = 0;
        Record curBuilding = null;
        while (true) {
            if (!inpnut.hasNextLine() && minHeap.isEmpty() && curBuilding == null) {
                break;
            }
            // read next commnad
            if (time == -1 && inpnut.hasNextLine()) {
                String timeStr = inpnut.next();
                time = Integer.parseInt(timeStr.split(":")[0]);
            }
            // command execution time reached
            if (timeCounter == time) {
                String cmdAndParams = inpnut.nextLine().trim();
//                System.out.println("\n" + cmdAndParams);
                execCmd(cmdAndParams);
                time = -1;
            }
            timeCounter++;

            // update working building
            if (curBuilding == null) {
                // has nothing to do
                if (minHeap.isEmpty()) {
                    continue;
                }
                curBuilding = minHeap.extractMin();
                workDaysInBuilding = 0;
            }
            // increment executed time
            curBuilding.increExecutedTime(1);
            workDaysInBuilding += 1;
            if (curBuilding.isFinished()) {
                redBlackTree.remove(curBuilding);
                System.out.printf("(%d,%d)\n", curBuilding.getBuildingNum(), timeCounter);
                curBuilding = null;
                continue;
            }

            if (workDaysInBuilding == 5) {
                minHeap.insert(curBuilding);
                curBuilding = null;
            }
        }
//        System.out.printf("city build finished in %d days\n", timeCounter);
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.out.println("Usage: java risingCity file_name");
            System.exit(-1);
        }
        new risingCity().run(args[0]);
    }
}
