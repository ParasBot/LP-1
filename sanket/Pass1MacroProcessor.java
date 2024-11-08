import java.io.*;
import java.util.*;

public class Pass1MacroProcessor {

    static class MNTEntry {
        String macroName;
        int mdtIndex;
        int numParams;

        MNTEntry(String macroName, int mdtIndex, int numParams) {
            this.macroName = macroName;
            this.mdtIndex = mdtIndex;
            this.numParams = numParams;
        }
    }

    static List<String> MDT = new ArrayList<>();
    static Map<String, MNTEntry> MNT = new HashMap<>();
    static Map<String, List<String>> ALA = new HashMap<>();
    static List<String> intermediateCode = new ArrayList<>();
    static int mdtPointer = 0;  // Keeps track of MDT index

    // Process the macro file
    public static void processMacro(List<String> inputLines) {
        boolean isMacroDefinition = false;
        String currentMacro = "";
        List<String> currentALA = new ArrayList<>();
        
        for (String line : inputLines) {
            String[] tokens = line.trim().split("\\s+");
            
            if (tokens[0].equals("START")) {
                intermediateCode.add(line);
            } 
            else if (tokens[0].equals("MACRO")) {
                isMacroDefinition = true;
                currentALA.clear();
            } 
            else if (tokens[0].equals("MEND")) {
                isMacroDefinition = false;
                MDT.add("MEND");
                intermediateCode.add("MEND");
                mdtPointer++;
            } 
            else if (isMacroDefinition) {
                // First line of macro is the macro name and parameters
                if (currentMacro.isEmpty()) {
                    currentMacro = tokens[0];
                    for (int i = 1; i < tokens.length; i++) {
                        currentALA.add(tokens[i]);
                    }
                    ALA.put(currentMacro, new ArrayList<>(currentALA));
                    MNT.put(currentMacro, new MNTEntry(currentMacro, mdtPointer, currentALA.size()));
                } 
                else {
                    MDT.add(line);
                    mdtPointer++;
                }
            } 
            else if (MNT.containsKey(tokens[0])) {
                // Macro call - mark for expansion in Pass-II
                intermediateCode.add(line);
            } 
            else {
                intermediateCode.add(line);  // Normal instruction
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // Input source lines
        List<String> inputLines = Arrays.asList(
            "START",
            "MACRO",
            "INCR &ARG3 &ARG2",
            "ADD AREG, &ARG1",
            "MOVER BREG, &ARG1",
            "MEND",
            "MACRO",
            "PVG &ARG2 &ARG1",
            "SUB AREG, &ARG2",
            "MOVER CREG, &ARG1",
            "MEND",
            "INCR",
            "DECR",
            "DATA2",
            "END"
        );

        // Process the macro definitions and generate tables
        processMacro(inputLines);

        // Print MNT
        System.out.println("Macro Name Table (MNT):");
        for (Map.Entry<String, MNTEntry> entry : MNT.entrySet()) {
            System.out.println("Macro Name: " + entry.getKey() + ", MDT Index: " + entry.getValue().mdtIndex + ", Num Params: " + entry.getValue().numParams);
        }

        // Print MDT
        System.out.println("\nMacro Definition Table (MDT):");
        for (int i = 0; i < MDT.size(); i++) {
            System.out.println("MDT[" + i + "]: " + MDT.get(i));
        }

        // Print ALA
        System.out.println("\nArgument List Array (ALA):");
        for (Map.Entry<String, List<String>> entry : ALA.entrySet()) {
            System.out.println("Macro: " + entry.getKey() + " -> " + entry.getValue());
        }

        // Print Intermediate Code
        System.out.println("\nIntermediate Code:");
        for (String line : intermediateCode) {
            System.out.println(line);
        }
    }
}
