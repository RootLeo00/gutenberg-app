import java.util.Scanner;

public class Main {
    public static void main(String[] arg) {
        System.out.println("Welcome to Bogota, Mr. Thomas Anderson.");
        //REGEX
        RegEx regEx;
        if (arg.length!=0) {
            regEx = new RegEx(arg[0]);
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("  >> Please enter a regEx: ");
            //check scanner next
            if(!scanner.hasNext()) {
                System.err.println("  >> ERROR: empty regEx.");
                return;
            }
            else{
                regEx = new RegEx(scanner.next());
            }
        }
        System.out.println("  >> Parsing regEx \""+ regEx.getRegEx()+"\".");
        System.out.println("  >> ...");

        RegExTree regexTree = null;
        if (regEx.getRegEx().length()<1) {
            System.err.println("  >> ERROR: empty regEx.");
        } else {
            System.out.print("  >> ASCII codes: ["+(int) regEx.getRegEx().charAt(0));
            for (int i = 1; i< regEx.getRegEx().length(); i++) System.out.print(","+(int) regEx.getRegEx().charAt(i));
            System.out.println("].");
            try {
                regexTree = regEx.parse();
                System.out.println("  >> Tree result: "+regexTree.toString()+".");
            } catch (Exception e) {
                System.err.println("  >> ERROR: syntax error for regEx \""+ regEx +"\".");
            }
        }

        System.out.println("  >> ...");
        System.out.println("  >> Parsing completed.");

        //print leaves from last node
/*        System.out.println("  >> Printing leaves from last node...");
        printLeavesFromLastToRoot(regexTree);*/

        if(regexTree != null) {
            System.out.println("  >> Converting to NDFA...");
            RegExToNFAConverter converter = new RegExToNFAConverter();
            try{
                NDFA ndfa = converter.convert(regexTree);
                System.out.println("  >> NDFA: \n"+ndfa.toString()+"\n");

                ndfa = ndfa.rename(ndfa);
                System.out.println("  >> RENAMED NDFA: \n"+ndfa.toString()+"\n");

            } catch (Exception e) {
                System.err.println("  >> ERROR: syntax error for regEx \""+ regEx.getRegEx()+"\".");
            }
            System.out.println("  >> ...");
            System.out.println("  >> NDFA completed.");

        }
        System.out.println("Goodbye Mr. Anderson.");
    }

    public static void printLeavesFromLastToRoot(RegExTree regexTree) {
        if (regexTree == null) return;
        System.out.print(regexTree.toString() + " ");
        System.out.print(regexTree.root + " ");
        System.out.print(regexTree.subTrees.size() + "\n");


        // Check if it's a leaf node (no children)
        if (regexTree.subTrees.isEmpty()) {
            System.out.print("leaf: "+ regexTree.toString() + " ");
            return;
        }
        // Traverse the right subtree (last leaf)
        if(regexTree.subTrees.size() > 1) {
            printLeavesFromLastToRoot(regexTree.subTrees.get(1));
        }

        // Traverse the left subtree (move up towards the root)
        printLeavesFromLastToRoot(regexTree.subTrees.get(0));
    }

}
