import java.util.*;

public class PageReplacement1 {
    // Method to simulate FIFO page replacement
    public static void fifoPageReplacement(int[] pages, int numFrames) {
        Set<Integer> frameSet = new HashSet<>();
        Queue<Integer> frameQueue = new LinkedList<>();
        int pageFaults = 0;

        System.out.println("FIFO Page Replacement:");
        for (int page : pages) {
            if (!frameSet.contains(page)) {
                if (frameQueue.size() == numFrames) {
                    int removedPage = frameQueue.poll();
                    frameSet.remove(removedPage);
                }
                frameQueue.add(page);
                frameSet.add(page);
                pageFaults++;
                System.out.println("Page fault for page: " + page);
            } else {
                System.out.println("No page fault for page: " + page);
            }
        }
        System.out.println("Total page faults (FIFO): " + pageFaults);
    }

    // Method to simulate LRU page replacement
    public static void lruPageReplacement(int[] pages, int numFrames) {
        Set<Integer> frameSet = new HashSet<>();
        Map<Integer, Integer> lruMap = new HashMap<>();
        int pageFaults = 0;
        int time = 0;

        System.out.println("\nLRU Page Replacement:");
        for (int page : pages) {
            if (!frameSet.contains(page)) {
                if (frameSet.size() == numFrames) {
                    int lruPage = getLRUPage(lruMap);
                    frameSet.remove(lruPage);
                    lruMap.remove(lruPage);
                }
                frameSet.add(page);
                pageFaults++;
                System.out.println("Page fault for page: " + page);
            } else {
                System.out.println("No page fault for page: " + page);
            }
            lruMap.put(page, time++);
        }
        System.out.println("Total page faults (LRU): " + pageFaults);
    }

    // Helper method to find the least recently used page
    private static int getLRUPage(Map<Integer, Integer> lruMap) {
        int lruPage = -1;
        int lruTime = Integer.MAX_VALUE;

        for (Map.Entry<Integer, Integer> entry : lruMap.entrySet()) {
            if (entry.getValue() < lruTime) {
                lruTime = entry.getValue();
                lruPage = entry.getKey();
            }
        }
        return lruPage;
    }

    public static void main(String[] args) {
        // Example input
        int[] pages = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
        int numFrames = 3;

        fifoPageReplacement(pages, numFrames);
        lruPageReplacement(pages, numFrames);
    }
}
