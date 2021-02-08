import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Project2 {
	static int i = 0;
	static ArrayList<Token> arrList = new ArrayList<Token>();

	public static void main(String[] args) throws Exception {
		// command line argument for fileName

		// ArrayList to store tokens for Parser in sequential order (token = type of
		// token and name of token)

		///// try-catch block
		///////////
		try {
			File file = new File(args[0]);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String input;
			Boolean blockComment = false;

			// PATTERNS
			Pattern commentBlockEnd = Pattern.compile("^\\*/");
			Pattern commentBlockEndSameLine = Pattern.compile("\\*/");
			Pattern commentBlockStart = Pattern.compile("^/\\*");
			Pattern commentLine = Pattern.compile("^//");
			Pattern returnKW = Pattern.compile("^return");
			Pattern whileKW = Pattern.compile("^while");
			Pattern voidKW = Pattern.compile("^void");
			Pattern elseKW = Pattern.compile("^else");
			Pattern intKW = Pattern.compile("^int");
			Pattern ifKW = Pattern.compile("^if");
			Pattern symbol1 = Pattern.compile("^==|^<=|^>=|^!=");
			Pattern symbol2 = Pattern.compile("^\\+|^\\-|^\\*|^/|^=|^<|^>|^;|^,|^\\(|^\\)|^\\[|^\\]|^\\{|^\\}");
			Pattern id = Pattern.compile("(^[a-zA-Z]+)");
			Pattern error = Pattern.compile(
					"^([a-zA-Z]|[\\d]|\\+|^\\-|^\\*|^/|^=|^<|^>|^;|^,|^\\(|^\\)|^\\[|^\\]|^\\{|^\\}|==|^<=|^>=|^!=)");
			Pattern integer = Pattern.compile("^(\\d+)");
			Pattern kwID = Pattern.compile(
					"^return[a-zA-Z]*[\\d]+|^return[\\d]*[a-zA-Z]+|^while[a-zA-Z]*[\\d]+|^while[\\d]*[a-zA-Z]+|^void[a-zA-Z]*[\\d]+|^void[\\d]*[a-zA-Z]+|^else[a-zA-Z]*[\\d]+|^else[\\d]*[a-zA-Z]+|^int[a-zA-Z]*[\\d]+|^int[\\d]*[a-zA-Z]+|^if[a-zA-Z]*[\\d]+|^if[\\d]*[a-zA-Z]+");
			// matcher for checking regex
			Matcher check;
			// index for modifying substrings
			int matchPos;

			while ((input = br.readLine()) != null) {
				// iterates to next line if there is nothing but blank space on a Line.
				if (input.isEmpty()) {
					continue;
				}

				// only prints line input if there is an input on the line besides blank space
				if (!input.trim().isEmpty()) {
					// System.out.println("INPUT: " + input);
				}
				outerLoop: while (input != null) {
					// trims whitespace from front and back of string
					input = input.trim();

					// iterates to next line if there is nothing but blank space on a Line.
					if (input.isEmpty()) {
						break outerLoop;
					}

					// checks for end of comment block */
					if (blockComment == true) {
						check = commentBlockEnd.matcher(input);
						if (check.find()) {
							// upon finding */, toggles blockComment to false and moves string forwards
							blockComment = false;
							matchPos = check.end();
							input = input.substring(matchPos);
							continue;
						}
					}

					// checks for start of comment block /*
					check = commentBlockStart.matcher(input);
					if (check.find()) {
						// upon finding /*, toggles blockComment to true and moves string forwards
						blockComment = true;
						matchPos = check.end();
						input = input.substring(matchPos);
						continue;
					}

					// if inside BlockComment, logic to try to find another */ on the line
					if (blockComment == true) {
						// check if the next term ends the comment block */
						check = commentBlockEnd.matcher(input);
						// while the next term is not the end comment block,
						while (!check.find()) {
							// check if the entire line contains an end of comment block
							check = commentBlockEndSameLine.matcher(input);
							// if no */ exists on the entire line, the entire line is a comment
							if (!check.find()) {
								break outerLoop;
							}
							// if */ does exist on the line, it will ignore the string up to that point
							matchPos = check.start();
							input = input.substring(matchPos);
							// then check again for the end of comment block
							check = commentBlockEnd.matcher(input);
						}
						continue;
					}

					// checks if the string begins with //, then the entire line is a comment
					check = commentLine.matcher(input);
					if (check.find()) {
						break outerLoop;
					}

					// regex for ID starting with a KW
					check = kwID.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println("ID: " + input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("id", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = kwID.matcher(input);
						continue;
					}

					// KW regexes
					check = returnKW.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println("KW: " + input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("return", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = returnKW.matcher(input);
						continue;
					}

					check = whileKW.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println("KW: " + input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("while", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = whileKW.matcher(input);
						continue;
					}

					check = voidKW.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println("KW: " + input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("void", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = voidKW.matcher(input);
						continue;
					}

					check = elseKW.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println("KW: " + input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("else", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = elseKW.matcher(input);
						continue;
					}

					check = intKW.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println("KW: " + input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("int", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = intKW.matcher(input);
						continue;
					}

					check = ifKW.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println("KW: " + input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("if", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = ifKW.matcher(input);
						continue;
					}

					// ID regexes
					check = id.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println("ID: " + input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("id", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = id.matcher(input);
						continue;
					}

					// 2-length symbol regexes
					check = symbol1.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println(input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("symbol", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = symbol1.matcher(input);
						continue;
					}

					// 1-length symbol regexes
					check = symbol2.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println(input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("symbol", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = symbol2.matcher(input);
						continue;
					}

					// int regexes
					check = integer.matcher(input);
					if (check.find()) {
						matchPos = check.end();
						// System.out.println("INT: " + input.substring(check.start(), matchPos));
						// add to arraylist
						arrList.add(new Token("num", input.substring(check.start(), matchPos)));
						input = input.substring(matchPos);
						input = input.trim();
						check = integer.matcher(input);
						continue;
					}

					check = error.matcher(input);
					if (!check.find()) {
						// matchPos = check.end();
						// System.out.println("ERROR: " + input.substring(0, 1));
						// add to arraylist
						arrList.add(new Token("error", input.substring(0, 1)));
						input = input.substring(1);
						input = input.trim();
						check = error.matcher(input);
						continue;
					}

					continue;

				} // end of while- line empty
			} ///////////////////// end of while- no more lines to read

			arrList.add(new Token("EOF", "$"));
			// System.out.println(arrList.get(0).tokenValue);
			// if (arrList.get(0).tokenValue.equals("int")) {
			// System.out.println("true");
			// }

			program();
			if (arrList.get(i).tokenValue == "$") {
				System.out.println("ACCEPT");
			} else {
				System.out.println("REJECT");
			}

		} catch (FileNotFoundException e) {
			System.out.println("File does not exist");
			System.exit(0);
		}
	} //////////////////////////// end of main

	// A
	static void program() {
		// B
		declarationList(); // B
	}

	// B
	static void declarationList() {
		// C B'
		// C B'
		declaration(); // C
		declarationListPrime(); // B'
	}

	// B'
	static void declarationListPrime() {
		// C B' | empty
		// C B'
		if (arrList.get(i).tokenValue.equals("int") || arrList.get(i).tokenValue.equals("void")) {
			declaration(); // C
			declarationListPrime(); // B'
		}
	}

	// C
	static void declaration() {
		// int id C' | void id C'
		// int id C'
		if (arrList.get(i).tokenValue.equals("int")) {
			i++; // int
			if (arrList.get(i).tokenType.equals("id")) {
				i++; // id
				declarationPrime(); // C'
			} else { // fails
				System.out.println("REJECT");
				System.exit(0);
			}
		} // void id C'
		else if (arrList.get(i).tokenValue.equals("void")) {
			i++; // int
			if (arrList.get(i).tokenType.equals("id")) {
				i++; // id
				declarationPrime(); // C'
			} else { // fails
				System.out.println("REJECT");
				System.exit(0);
			}
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// C'
	static void declarationPrime() {
		// D' | (G) J
		// (G) J
		if (arrList.get(i).tokenValue.equals("(")) {
			i++; // (
			params(); // G
			if (arrList.get(i).tokenValue.equals(")")) {
				i++; // )
				compoundStatement(); // J
			} else { // fails
				System.out.println("REJECT");
				System.exit(0);
			}
		} // D'
		else if (arrList.get(i).tokenValue.equals(";") || arrList.get(i).tokenValue.equals("[")) {
			variableDeclarationPrime(); // D'
		}
		// fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// D
	static void variableDeclaration() {
		// int id D' | void id D'
		// int id D'
		if (arrList.get(i).tokenValue.equals("int")) {
			i++; // int
			if (arrList.get(i).tokenType.equals("id")) {
				i++; // id
				variableDeclarationPrime(); // D'
			} else { // fails
				System.out.println("REJECT");
				System.exit(0);
			}
		} // void id D'
		else if (arrList.get(i).tokenValue.equals("void")) {
			i++; // void
			if (arrList.get(i).tokenType.equals("id")) {
				i++; // id
				variableDeclarationPrime(); // D'
			} else { // fails
				System.out.println("REJECT");
				System.exit(0);
			}
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// D'
	static void variableDeclarationPrime() {
		// ; | [num] ;
		// ;
		if (arrList.get(i).tokenValue.equals(";")) {
			i++; // ;
		} // [num] ;
		else if (arrList.get(i).tokenValue.equals("[")) {
			i++; // [
			if (arrList.get(i).tokenType.equals("num")) {
				i++; // num
				if (arrList.get(i).tokenValue.equals("]")) {
					i++; // ]
					if (arrList.get(i).tokenValue.equals(";")) {
						i++; // ;
					} // fails
					else {
						System.out.println("REJECT");
						System.exit(0);
					}
				} // fails
				else {
					System.out.println("REJECT");
					System.exit(0);
				}
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// E
	static void typeSpecifier() {
		// int | void not used
	}

	// F
	static void functionDeclaration() {
		// E id (G) J not used
	}

	// G
	static void params() {
		// int id I' H' | void G'
		// int id I' H'
		if (arrList.get(i).tokenValue.equals("int")) {
			i++; // int
			if (arrList.get(i).tokenType.equals("id")) {
				i++; // id
				paramPrime(); // I'
				paramListPrime(); // H'
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // void G'
		else if (arrList.get(i).tokenValue.equals("void")) {
			i++; // void
			paramsPrime(); // G'
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}

	}

	// G'
	static void paramsPrime() {
		// id I' H' | empty
		if (arrList.get(i).tokenType.equals("id")) {
			i++; // id
			paramPrime(); // I'
			paramListPrime(); // H'
		}
	}

	// H
	static void paramList() {
		// I H' not used
		// param(); // I
		// paramListPrime(); // H'
	}

	// H'
	static void paramListPrime() {
		// , I H' | empty
		if (arrList.get(i).tokenValue.equals(",")) {
			i++; // ,
			param(); // I
			paramListPrime(); // H'
		}
	}

	// I
	static void param() {
		// int id I' | void id I'
		// int id I'
		if (arrList.get(i).tokenValue.equals("int")) {
			i++; // int
			if (arrList.get(i).tokenType.equals("id")) {
				i++; // id
				paramPrime(); // I'
			} else { // fails
				System.out.println("REJECT");
				System.exit(0);
			}
		} // void id I'
		else if (arrList.get(i).tokenValue.equals("void")) {
			i++; // void
			if (arrList.get(i).tokenType.equals("id")) {
				i++; // id
				paramPrime(); // I'
			} else { // fails
				System.out.println("REJECT");
				System.exit(0);
			}
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// I'
	static void paramPrime() {
		// empty | []
		// []
		if (arrList.get(i).tokenValue.equals("[")) {
			i++; // [
			if (arrList.get(i).tokenValue.equals("]")) {
				i++; // ]
			}
		}
	}

	// J
	static void compoundStatement() {
		// { K L }
		if (arrList.get(i).tokenValue.equals("{")) {
			i++; // {
			localDeclaration(); // K
			statementList(); // L
			if (arrList.get(i).tokenValue.equals("}")) {
				i++; // }
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// K
	static void localDeclaration() {
		// K'
		localDeclarationPrime(); // K'
	}

	// K'
	static void localDeclarationPrime() {
		// D K' | empty
		// D K'
		if (arrList.get(i).tokenValue.equals("int") || arrList.get(i).tokenValue.equals("void")) {
			variableDeclaration(); // D
			localDeclarationPrime(); // K'
		}
	}

	// L
	static void statementList() {
		// L'
		statementListPrime(); // L'
	}

	// L'
	static void statementListPrime() {
		// M L' | empty
		// M L'
		if (arrList.get(i).tokenValue.equals(";") || arrList.get(i).tokenType.equals("id")
				|| arrList.get(i).tokenValue.equals("(") || arrList.get(i).tokenType.equals("num")
				|| arrList.get(i).tokenValue.equals("{") || arrList.get(i).tokenValue.equals("if")
				|| arrList.get(i).tokenValue.equals("while") || arrList.get(i).tokenValue.equals("return")) {
			statement(); // M
			statementListPrime(); // L'
		}
	}

	// M
	static void statement() {
		// N | J | O | P | Q
		// N
		if (arrList.get(i).tokenValue.equals(";") || arrList.get(i).tokenType.equals("id")
				|| arrList.get(i).tokenType.equals("num") || arrList.get(i).tokenValue.equals("(")) {
			expressionStatement(); // N
		} // J
		else if (arrList.get(i).tokenValue.equals("{")) {
			compoundStatement(); // J
		} // O
		else if (arrList.get(i).tokenValue.equals("if")) {
			selectionStatement(); // O
		} // P
		else if (arrList.get(i).tokenValue.equals("while")) {
			iterationStatement(); // P
		} // Q
		else if (arrList.get(i).tokenValue.equals("return")) {
			returnStatement(); // Q
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// N
	static void expressionStatement() {
		// R ; | ;
		// R ;
		if (arrList.get(i).tokenType.equals("id") || arrList.get(i).tokenType.equals("num")
				|| arrList.get(i).tokenValue.equals("(")) {
			expression(); // R
			if (arrList.get(i).tokenValue.equals(";")) {
				i++; // ;
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // ;
		else if (arrList.get(i).tokenValue.equals(";")) {
			i++; // ;
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// O
	static void selectionStatement() {
		// if (R) M O'
		if (arrList.get(i).tokenValue.equals("if")) {
			i++; // if
			if (arrList.get(i).tokenValue.equals("(")) {
				i++; // (
				expression(); // R
				if (arrList.get(i).tokenValue.equals(")")) {
					i++; // )
					statement(); // M
					selectionStatementPrime(); // O'
				} // fails
				else {
					System.out.println("REJECT");
					System.exit(0);
				}
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// O'
	static void selectionStatementPrime() {
		// empty | else R
		// else R
		if (arrList.get(i).tokenValue.equals("else")) {
			i++; // else
			statement(); // M
		}
	}

	// P
	static void iterationStatement() {
		// while (R) M
		if (arrList.get(i).tokenValue.equals("while")) {
			i++; // while
			if (arrList.get(i).tokenValue.equals("(")) {
				i++; // (
				expression(); // R
				if (arrList.get(i).tokenValue.equals(")")) {
					i++; // )
					statement(); // M
				} // fails
				else {
					System.out.println("REJECT");
					System.exit(0);
				}
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// Q
	static void returnStatement() {
		// return Q'
		if (arrList.get(i).tokenValue.equals("return")) {
			i++; // return
			returnStatementPrime(); // Q'
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// Q'
	static void returnStatementPrime() {
		// ; | R;
		// ;
		if (arrList.get(i).tokenValue.equals(";")) {
			i++; // ;
		} else {
			expression(); // R
			if (arrList.get(i).tokenValue.equals(";")) {
				i++; // ;
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		}
	}

	// R
	static void expression() {
		// id R' | (R) X' V' T' | num X' V' T'
		// id R'
		if (arrList.get(i).tokenType.equals("id")) {
			i++; // id
			expressionPrime(); // R'
		} // (R) X' V' T'
		else if (arrList.get(i).tokenValue.equals("(")) {
			i++; // (
			expression(); // R
			if (arrList.get(i).tokenValue.equals(")")) {
				i++; // )
				termPrime(); // X'
				additiveExpressionPrime(); // V'
				simpleExpressionPrime(); // T'
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // num X' V' T'
		else if (arrList.get(i).tokenType.equals("num")) {
			i++; // num
			termPrime(); // X'
			additiveExpressionPrime(); // V'
			simpleExpressionPrime(); // T'
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// R'
	static void expressionPrime() {
		// S' R'' | (theta) X' V' T'
		// (theta) X' V' T'
		if (arrList.get(i).tokenValue.equals("(")) {
			i++; // (
			args(); // theta
			if (arrList.get(i).tokenValue.equals(")")) {
				i++; // )
				termPrime(); // X'
				additiveExpressionPrime(); // V'
				simpleExpressionPrime(); // T'
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // S' R''
		else {
			variablePrime(); // S'
			expressionPrimePrime(); // R''
		}
	}

	// R''
	static void expressionPrimePrime() {
		// = R | X' V' T'
		// = R
		if (arrList.get(i).tokenValue.equals("=")) {
			i++; // =
			expression(); // R
		} else {
			termPrime(); // X'
			additiveExpressionPrime(); // V'
			simpleExpressionPrime(); // T'
		}
	}

	// S
	static void variable() {
		// id S' not used
	}

	// S'
	static void variablePrime() {
		// empty | [R]
		if (arrList.get(i).tokenValue.equals("[")) {
			i++; // [
			expression(); // R
			if (arrList.get(i).tokenValue.equals("]")) {
				i++; // ]
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		}
	}

	// T
	static void simpleExpression() {
		// V T'
		additiveExpression();
		simpleExpressionPrime();
	}

	// T'
	static void simpleExpressionPrime() {
		// U V | empty
		if (arrList.get(i).tokenValue.equals("<") || arrList.get(i).tokenValue.equals("<=")
				|| arrList.get(i).tokenValue.equals(">") || arrList.get(i).tokenValue.equals(">=")
				|| arrList.get(i).tokenValue.equals("==") || arrList.get(i).tokenValue.equals("!=")) {
			relationalOperator(); // U
			additiveExpression(); // V
		}
	}

	// U
	static void relationalOperator() {
		// <= | < | > | >= | == | !=
		if (arrList.get(i).tokenValue.equals("<") || arrList.get(i).tokenValue.equals("<=")
				|| arrList.get(i).tokenValue.equals(">") || arrList.get(i).tokenValue.equals(">=")
				|| arrList.get(i).tokenValue.equals("==") || arrList.get(i).tokenValue.equals("!=")) {
			i++; // <= | < | > | >= | == | !=
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// V
	static void additiveExpression() {
		// X V'
		term(); // X
		additiveExpressionPrime(); // V'
	}

	// V'
	static void additiveExpressionPrime() {
		// W X V' | empty
		// W X V'
		if (arrList.get(i).tokenValue.equals("+") || arrList.get(i).tokenValue.equals("-")) {
			additiveOperator(); // W
			term(); // X
			additiveExpressionPrime(); // V'
		}
	}

	// W
	static void additiveOperator() {
		// + | -
		if (arrList.get(i).tokenValue.equals("+") || arrList.get(i).tokenValue.equals("-")) {
			i++; // + | -
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// X
	static void term() {
		// Z X'
		factor(); // Z
		termPrime(); // X'
	}

	// X'
	static void termPrime() {
		// Y Z X' | empty
		// Y Z X'
		if (arrList.get(i).tokenValue.equals("*") || arrList.get(i).tokenValue.equals("/")) {
			multiplicativeOperator(); // W
			factor(); // X
			termPrime(); // V'
		}
	}

	// Y
	static void multiplicativeOperator() {
		// * | /
		if (arrList.get(i).tokenValue.equals("*") || arrList.get(i).tokenValue.equals("/")) {
			i++; // + | -
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// Z
	static void factor() {
		// (R) | num | id Z'
		// (R)
		if (arrList.get(i).tokenValue.equals("(")) {
			i++; // (
			expression(); // R
			if (arrList.get(i).tokenValue.equals(")")) {
				i++; // )
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // num
		else if (arrList.get(i).tokenType.equals("num")) {
			i++; // num
		} // id Z'
		else if (arrList.get(i).tokenType.equals("id")) {
			i++; // id
			factorPrime(); // Z'
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// Z'
	static void factorPrime() {
		// S' | (theta)
		if (arrList.get(i).tokenValue.equals("(")) {
			i++; // (
			args(); // thera
			if (arrList.get(i).tokenValue.equals(")")) {
				i++; // )
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // S'
		else {
			variablePrime(); // S'
		}

	}

	// PI
	static void call() {
		// id (theta)
		if (arrList.get(i).tokenType.equals("id")) {
			i++; // id
			if (arrList.get(i).tokenValue.equals("(")) {
				i++; // (
				args(); // theta
				if (arrList.get(i).tokenValue.equals(")")) {
					i++; // )
				} // fails
				else {
					System.out.println("REJECT");
					System.exit(0);
				}
			} // fails
			else {
				System.out.println("REJECT");
				System.exit(0);
			}
		} // fails
		else {
			System.out.println("REJECT");
			System.exit(0);
		}
	}

	// THETA
	static void args() {
		// psi | empty
		if (arrList.get(i).tokenType.equals("id") || arrList.get(i).tokenValue.equals("(")
				|| arrList.get(i).tokenType.equals("num")) {
			argList(); // psi
		}
	}

	// PSI
	static void argList() {
		// R psi'
		expression(); // R
		argListPrime(); // psi'
	}

	// PSI'
	static void argListPrime() {
		// , R psi' | empty
		// , R psi'
		if (arrList.get(i).tokenValue.equals(",")) {
			i++; // ,
			expression(); // R
			argListPrime(); // psi'
		}
	}

} /// main class