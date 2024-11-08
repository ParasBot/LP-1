import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Pass2 {
    ArrayList<TableRow> SYMTAB, LITTAB;

    public Pass2() {
        SYMTAB = new ArrayList<>();
        LITTAB = new ArrayList<>();
    }



    public void readtables() {
        BufferedReader br;
        String line;
        try {
            // Read SYMTAB.txt
            br = new BufferedReader(new FileReader("SYMTAB.txt"));
            while ((line = br.readLine()) != null) {
                String parts[] = line.trim().split("\\s+");
                if (parts.length != 3) {
                    System.err.println("Invalid SYMTAB entry: " + line);
                    continue;
                }
                // Correctly assign symbol, address, and index
                SYMTAB.add(new TableRow(parts[0], Integer.parseInt(parts[2]), Integer.parseInt(parts[1])));
            }
            br.close();

            // Read LITTAB.txt
            br = new BufferedReader(new FileReader("LITTAB.txt"));
            int litIndex = 1; // Initialize literal index
            while ((line = br.readLine()) != null) {
                String parts[] = line.trim().split("\\s+");
                if (parts.length != 2) {
                    System.err.println("Invalid LITTAB entry: " + line);
                    continue;
                }
                // Assign literal, address, and index based on line order
                LITTAB.add(new TableRow(parts[0], Integer.parseInt(parts[1]), litIndex));
                litIndex++;
            }
            br.close();
        } catch (Exception e) {
            System.err.println("Error reading tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void generateCode(String filename) throws Exception {
        readtables();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        BufferedWriter bw = new BufferedWriter(new FileWriter("PASS2.txt"));
        String line, code;

        while ((line = br.readLine()) != null) {
            String parts[] = line.trim().split("\\s+");

            // Skip assembler directives and certain DL directives
            if (parts[0].contains("AD") || parts[0].contains("DL,02")) {
                bw.write("\n");
                continue;
            }

            if (parts.length == 2) {
                // Declarative (DC) instruction
                if (parts[0].contains("DL")) {
                    parts[0] = parts[0].replaceAll("[^0-9]", ""); // Extract numeric opcode
                    if (Integer.parseInt(parts[0]) == 1) {
                        int constant = Integer.parseInt(parts[1].replaceAll("[^0-9]", "")); // Extract constant
                        code = "00\t0\t" + String.format("%03d", constant) + "\n";
                        bw.write(code);
                    }
                }
                // Imperative (IS) instruction with symbol or literal
                else if (parts[0].contains("IS")) {
                    int opcode = Integer.parseInt(parts[0].replaceAll("[^0-9]", "")); // Extract numeric opcode
                    if (opcode == 10) {
                        if (parts[1].contains("S")) {
                            int symIndex = Integer.parseInt(parts[1].replaceAll("[^0-9]", "")); // Extract symbol index
                            code = String.format("%02d", opcode) + "\t0\t" + String.format("%03d", SYMTAB.get(symIndex - 1).getAddress()) + "\n";
                            bw.write(code);
                        } else if (parts[1].contains("L")) {
                            int litIndex = Integer.parseInt(parts[1].replaceAll("[^0-9]", "")); // Extract literal index
                            code = String.format("%02d", opcode) + "\t0\t" + String.format("%03d", LITTAB.get(litIndex - 1).getAddress()) + "\n";
                            bw.write(code);
                        }
                    }
                }
            }
            // Single IS opcode with no operands
            else if (parts.length == 1 && parts[0].contains("IS")) {
                int opcode = Integer.parseInt(parts[0].replaceAll("[^0-9]", "")); // Extract numeric opcode
                code = String.format("%02d", opcode) + "\t0\t000\n";
                bw.write(code);
            }
            // All other IS instructions with register and operand
            else if (parts[0].contains("IS") && parts.length == 3) {
                int opcode = Integer.parseInt(parts[0].replaceAll("[^0-9]", "")); // Extract numeric opcode
                int regcode = Integer.parseInt(parts[1].replaceAll("[^0-9]", "")); // Extract register code
                if (parts[2].contains("S")) {
                    int symIndex = Integer.parseInt(parts[2].replaceAll("[^0-9]", "")); // Extract symbol index
                    code = String.format("%02d", opcode) + "\t" + regcode + "\t" + String.format("%03d", SYMTAB.get(symIndex - 1).getAddress()) + "\n";
                    bw.write(code);
                } else if (parts[2].contains("L")) {
                    int litIndex = Integer.parseInt(parts[2].replaceAll("[^0-9]", "")); // Extract literal index
                    code = String.format("%02d", opcode) + "\t" + regcode + "\t" + String.format("%03d", LITTAB.get(litIndex - 1).getAddress()) + "\n";
                    bw.write(code);
                }
            }
        }
        bw.close();
        br.close();
    }
    public static void main(String[] args) {
        Pass2 pass2 = new Pass2();
        try {
            pass2.generateCode("IC.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} // End of class Pass2
