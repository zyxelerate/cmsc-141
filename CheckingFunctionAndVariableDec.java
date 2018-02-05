package mp1;

import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.lang3.math.NumberUtils;

public class CheckingFunctionAndVariableDec {
//	public enum DataType {
//		CHAR, INT , DOUBLE ,FLOAT
//	}
//
//	public enum Operations {
//		PLUS, MINUS, TIMES, DIVIDE
//	}
//
//	public enum Punctuations {
//		SEMICOLON, COMMA, EQUALS
//	}
	
	private static final String FILENAME = "src/mp1/test";
	private static final String CHAR = "char";
	private static final String INT = "int";
	private static final String DOUBLE = "double";
	private static final String FLOAT = "float";
	private static final String COMMA = ",";
	private static final String SEMICOLON = ";";
	private static final String EQUALS = "=";
	private static final String PLUS = "+";
	private static final String MINUS = "-";
	private static final String TIMES = "*";
	private static final String DIVIDE = "/";
	private static final int[][] variableDiagram = {{1,4,4,4,4,4,4,4},
 												    {4,2,4,4,4,4,4,4},
												    {4,4,1,3,4,5,4,4},
												    {1,2,4,3,4,4,4,4},
												    {4,4,4,4,4,4,4,4},
												    {4,2,4,4,4,4,6,4},
												    {4,2,1,3,4,4,2,5}};  

		
	public static void main(String[] args) {
		//declare a local variable that is a double array
		//reading some file here
		//then call function for initialize strings
		try {
			BufferedReader bufferRead = new BufferedReader(new FileReader(FILENAME));
			int testcases = returnNumberofCases(bufferRead);			
			String[] variableAndfunctionLines = new String[testcases];
			String[][] tokenizedVarAndFuncLines = new String[testcases][];
			int[] varAndfuncChecker;
			variableAndfunctionLines = readingLinesfromFile(bufferRead, variableAndfunctionLines);
			variableAndfunctionLines = reformatingCommasWithSpace(variableAndfunctionLines);
			varAndfuncChecker = checksVarAndFuncStrings(variableAndfunctionLines);
			tokenizedVarAndFuncLines = tokenizeVariableAndFunctionLines(variableAndfunctionLines,tokenizedVarAndFuncLines);
			
			printResults(tokenizedVarAndFuncLines, varAndfuncChecker);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String[] reformatingCommasWithSpace(String[] variableAndfunctionLines) {
		//since in tokenizing our string, our only delimiter is space
		//in order to split them by space we need to add space before ',' and ';'
		//what if daghan ra kaayo ang test cases
		for(int i = 0; i < variableAndfunctionLines.length; i++) {
			variableAndfunctionLines[i] = variableAndfunctionLines[i].replace(COMMA, " " + COMMA);
			variableAndfunctionLines[i] = variableAndfunctionLines[i].replace(SEMICOLON, " " + SEMICOLON);
			variableAndfunctionLines[i] = variableAndfunctionLines[i].replace("(", " (");
			variableAndfunctionLines[i] = variableAndfunctionLines[i].replace(")", " )");
		}
		return variableAndfunctionLines;
	}
	
	//first line is always a number	
	public static int returnNumberofCases(BufferedReader bufferReadCase) throws IOException {
		String testCaseString = bufferReadCase.readLine();
		//testCaseString = testCaseString.substring(0, testCaseString.length() - 1);
		return Integer.parseInt(testCaseString);
	}
	
	//i'm only considering variable and function declaration 
	public static String[] readingLinesfromFile(BufferedReader bufferRead, String[] variableAndfunctionLines) throws IOException {
		//buffer read and something
		String currentLine = "";
		for(int i = 0; (currentLine = bufferRead.readLine()) != null; i++) { variableAndfunctionLines[i] = currentLine; }
		bufferRead.close();
		return variableAndfunctionLines;
	}
	
	public static String[][] tokenizeVariableAndFunctionLines(String[] variableAndfunctionLines, String[][] initVarAndFuncLinesToken) {
		StringTokenizer tokenizer;
		for(int i = 0; i < variableAndfunctionLines.length; i++) {
			int j = 0;
			tokenizer = new StringTokenizer(variableAndfunctionLines[i], " ");
			initVarAndFuncLinesToken[i] = new String[tokenizer.countTokens()];
			while(tokenizer.hasMoreTokens()) {
				initVarAndFuncLinesToken[i][j] = tokenizer.nextToken();
				j++;
			}
		}
		return initVarAndFuncLinesToken;
	}

	public static int[] checksVarAndFuncStrings(String[] variableAndfunctionLines) {
		int[] varAndFuncChecker = new int[variableAndfunctionLines.length];
		for(int i = 0; i < variableAndfunctionLines.length; i++) {
			if(variableAndfunctionLines[i].indexOf('}') >= 0) { varAndFuncChecker[i] = 3;  } 
			else if(variableAndfunctionLines[i].indexOf(')') >= 0) { varAndFuncChecker[i] = 2; } 
			else { varAndFuncChecker[i] = 1; }
		}
		return varAndFuncChecker;
	}
	
	public static int returnStateVariable(String tokenValue) {
		//TODO check if variable is declared more than once
		if(tokenValue.equals(CHAR) || tokenValue.equals(INT) || tokenValue.equals(DOUBLE) || tokenValue.equals(FLOAT)) { return 0; }
		else if(tokenValue.equals(COMMA)) { return 2; } 
		else if(tokenValue.equals(SEMICOLON)) { return 3; } 
		else if(tokenValue.equals(EQUALS)) { return 5; } 
		else if(NumberUtils.isCreatable(tokenValue)) { return 6; }
		else if(tokenValue.equals(TIMES) || tokenValue.equals(DIVIDE) || tokenValue.equals(MINUS) || tokenValue.equals(PLUS)) { return 7; }
		else { return 1; } 
	}

	public static void printResults(String[][] tokenizedVarAndFuncLines, int[] varAndFuncChecker) {
		for(int i = 0; i < tokenizedVarAndFuncLines.length; i++) {
			boolean checkVarAndFunctionToken = changingVariableAndFunctionState(tokenizedVarAndFuncLines[i], varAndFuncChecker[i]);
			String printStatements = "";
			if(checkVarAndFunctionToken) { printStatements = "VALID"; } 
			else { printStatements = "INVALID"; }
			if(varAndFuncChecker[i] == 1) { 
				printStatements = printStatements + " VARIABLE DECLARATION";
			} else if(varAndFuncChecker[i] == 2) { //function declaration
				printStatements = printStatements + " FUNCTION DECLARATION";
			} else { //function definition 
				printStatements = printStatements + " FUNCTION DEFINITION";
			}
			System.out.println(printStatements);
		}
	}
		
	public static boolean changingVariableAndFunctionState(String[] OnetokenizedVarAndFuncLines, int flagIfVarAndFunc) { 
		int input = 0, state = 0; 
		for(int i = 0; i < OnetokenizedVarAndFuncLines.length; i++) {
			if(flagIfVarAndFunc == 1) {
				input = returnStateVariable(OnetokenizedVarAndFuncLines[i]);
				state = variableDiagram[state][input];
			} else if(flagIfVarAndFunc == 2) {
				//TODO function declaration part here
			} else {
				//TODO function definition part here;
			}			
			if(state == 4) {
				return false;
			}
		}
		return true;
	}
}
