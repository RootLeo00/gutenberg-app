import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] arg) {
        System.out.println("Welcome to RegEx Automa");
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

        NDFA ndfa = null;
        if(regexTree != null) {
            System.out.println("  >> Converting to NDFA...");
            RegExToNFAConverter converter = new RegExToNFAConverter();
            try{
                ndfa = converter.convert(regexTree);
                System.out.println("  >> NDFA: \n"+ndfa.toString()+"\n");

                ndfa = ndfa.rename(ndfa);
                System.out.println("  >> RENAMED NDFA: \n"+ndfa.toString()+"\n");

            } catch (Exception e) {
                System.err.println("  >> ERROR: syntax error for regEx \""+ regEx.getRegEx()+"\".");
            }
            System.out.println("  >> ...");
            System.out.println("  >> NDFA completed.");

        }
        if(ndfa!=null){
            System.out.println("  >> Converting to DFA...");
            NDFAtoDFAConverter converter = new NDFAtoDFAConverter();
            DFA dfa = converter.convert(ndfa);
            System.out.println("  >> DFA: \n"+dfa.toString()+"\n");
            System.out.println("  >> Minimize DFA...");
            dfa = dfa.mergeStates(dfa);
            System.out.println("  >> DFA with merged states: \n"+dfa.toString()+"\n");
            dfa= dfa.deleteUnreachableStates(dfa);
            System.out.println("  >> DFA without unreachable states: \n"+dfa.toString()+"\n");
            System.out.println("  >> ...");
            System.out.println("  >> DFA completed.");

        }
        System.out.println("Author @RootLeo.");
        System.out.println("Code available at: https://github.com/RootLeo00/gutenberg-app/tree/master/regex-engine");
    }

}
