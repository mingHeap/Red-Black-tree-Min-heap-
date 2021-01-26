public class Record implements Comparable<Record> {
    private int executedTime;
    private int buildingNum;
    private int totalTime;

    public Record(int buildingNum, int totalTime, int executedTime) {
        this.buildingNum = buildingNum;
        this.totalTime = totalTime;
        this.executedTime = executedTime;
    }

    public Record(int buildingNum, int totalTime) {
        this(buildingNum, totalTime, 0);
    }

    public void increExecutedTime(int increTime) {
        this.executedTime += increTime;
    }

    public int getExecutedTime() {
        return executedTime;
    }

    public int getBuildingNum() {
        return buildingNum;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public boolean isFinished() {
        return totalTime == executedTime;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d)", buildingNum, executedTime, totalTime);
    }

    @Override
    public int compareTo(Record o) {
        if (this.getExecutedTime() - o.getExecutedTime() == 0) {
            return this.getBuildingNum() - o.getBuildingNum();
        } else {
            return this.getExecutedTime() - o.getExecutedTime();
        }

    }
}
