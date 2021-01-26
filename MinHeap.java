public class MinHeap {

    private static final int MAX_SIZE = 2000;
    private Record[] arr;
    private int capacity = MAX_SIZE + 1;
    private int nodeNum;
    private int minChild;

    public boolean isEmpty() {
        return nodeNum == 0;
    }

    public MinHeap() {
        nodeNum = 0;
        arr = new Record[capacity];
    }


    public void insert(Record value) {
        if (this.nodeNum < MAX_SIZE) {
            arr[nodeNum + 1] = value;
            nodeNum++;
            shiftUp(nodeNum);
        } else {
            System.out.println("out of space!");
        }

    }

    private void shiftUp(int i) {
        if ((i <= 1) || (arr[i].compareTo(arr[i / 2]) > 0)) {
            return;
        } else {
            swap(i, i / 2);
            shiftUp(i / 2);
        }
    }

    public Record extractMin() {
        Record min = arr[1];
        arr[1] = arr[nodeNum];
        nodeNum--;
        heapify(1);
        return min;
    }

    private void heapify(int i) {
        if (i <= nodeNum / 2) {
            minChild = getMinChild(2 * i, 2 * i + 1);
            if (arr[minChild].compareTo(arr[i]) < 0) {
                swap(minChild, i);
                heapify(minChild);
            }
        }

    }

    private int getMinChild(int i, int j) {
        if (arr[i].compareTo(arr[j]) > 0) {
            return j;
        } else {
            return i;
        }
    }

    private void swap(int i, int j) {
        Record tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}


