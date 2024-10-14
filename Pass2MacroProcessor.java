import java.io.*;
import java.util.*;

public class Pass2MacroProcessor {
    static class MNTEntry {
        String name;
        int mdtIndex;
        int numParams;

        MNTEntry(String name, int mdtIndex, int numParams) {
            this.name = name;
            this.mdtIndex = mdtIndex;
            this.numParams = numParams;
        }
    }

    // Function to read the Macro Name Table (MNT)
    public static Map<String, MNTEntry> readMNT(String filename) throws IOException {
        Map<String, MNTEntry> mnt = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            String name = parts[0];
            int mdtIndex = Integer.parseInt(parts[2]);
            int numParams = Integer.parseInt(parts[1]);
            mnt.put(name, new MNTEntry(name, mdtIndex, numParams));
        }
        reader.close();
        return mnt;
    }

    // Function to read the Macro Definition Table (MDT)
    public static List<String> readMDT(String filename) throws IOException {
        List<String> mdt = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line;
        while ((line = reader.readLine()) != null) {
            mdt.add(line);
        }
        reader.close();
        return mdt;
    }

    // Function to process the intermediate file and expand macros
    public static void processIntermediateFile(String inputFile, String outputFile, Map<String, MNTEntry> mnt, List<String> mdt) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            String macroName = parts[0];

            if (mnt.containsKey(macroName)) {
                // Expand the macro
                MNTEntry entry = mnt.get(macroName);
                String[] actualParams = parts[1].split(",");

                expandMacro(entry, actualParams, writer, mdt);
            } else {
                // Write non-macro lines as is
                writer.write(line);
                writer.newLine();
            }
        }
        reader.close();
        writer.close();
    }

    // Function to expand macros by replacing formal parameters with actual parameters
    public static void expandMacro(MNTEntry entry, String[] actualParams, BufferedWriter writer, List<String> mdt) throws IOException {
        int index = entry.mdtIndex;
        while (!mdt.get(index).equals("MEND")) {
            String mdtLine = mdt.get(index);

            // Replace formal parameters (#1, #2, etc.) with actual parameters
            for (int i = 0; i < entry.numParams; i++) {
                mdtLine = mdtLine.replace("#" + (i + 1), actualParams[i]);
            }
            writer.write(mdtLine);
            writer.newLine();
            index++;
        }
    }

    public static void main(String[] args) throws IOException {
        String intermediateFile = "Intermediate.txt";
        String mntFile = "Mnt.txt";
        String mdtFile = "Mdt.txt";
        String outputFile = "ExpandedCode.txt";

        Map<String, MNTEntry> mnt = readMNT(mntFile);
        List<String> mdt = readMDT(mdtFile);

        processIntermediateFile(intermediateFile, outputFile, mnt, mdt);

        System.out.println("Pass-II macro expansion completed. Check 'ExpandedCode.txt' for output.");
    }
}