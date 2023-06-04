package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JTextArea;

import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class translator {
	@SuppressWarnings("deprecation")
	static public boolean translate(JTextArea javaTextArea, JTextArea cTextArea) {
		System.out.println("Starting Translation");
		boolean success = false;

		String content = javaTextArea.getText();

		String translation = "";

		StringBuilder sb = new StringBuilder();
		ANTLRInputStream input = new ANTLRInputStream(content);
		antlr.JavaLexer lexer = new antlr.JavaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		antlr.JavaParser parser = new antlr.JavaParser(tokens);
		antlr.JavaListener listener = new antlr.JavaListener();

		ParseTree tree = parser.compilationUnit();
		
		
		ParseTreeWalker.DEFAULT.walk(listener, tree);
		int indent = 0, forDepth = 0;
		boolean forLoop = false;
		boolean arrayInit = false;
				translation += ("using System;\n");
		for (int i = 0; i < listener.tokens.size() - 1; i++) {
			if (listener.tokens.get(i).contentEquals("for")) {
				forLoop = true;
			}
			if (listener.tokens.get(i).contentEquals("(") && forLoop) {
				forDepth++;
			} else if (listener.tokens.get(i).contentEquals(")") && forLoop) {
				forDepth--;
				if (forDepth == 0) {
					forLoop = false;
				}
			}

			if (listener.tokens.get(i).contentEquals("=") && listener.tokens.get(i + 1).contentEquals("{")) {
				arrayInit = true;

			}
			if (listener.tokens.get(i).contentEquals("}") && arrayInit) {
				arrayInit = false;
			}

			translation += (listener.tokens.get(i));
			if (!listener.tokens.get(i).contentEquals(";") && !listener.tokens.get(i).contentEquals(".")
					&& !listener.tokens.get(i + 1).contentEquals(".") && !listener.tokens.get(i).contentEquals("(")
					&& !listener.tokens.get(i + 1).contentEquals("(") && !listener.tokens.get(i + 1).contentEquals(")")
					&& !listener.tokens.get(i + 1).contentEquals(";")) {
				translation += (" ");
			}

			if (listener.tokens.get(i).contentEquals(";")) {
				if (!forLoop) {
					translation += ("\n");
				}
				if (!listener.tokens.get(i + 1).contentEquals("}") && !forLoop) {
					for (int y = 0; y < indent; y++) {
						translation += ("    ");
					}
				} else if (!forLoop) {
					for (int y = 0; y < indent - 1; y++) {
						translation += ("    ");
					}
				} else if (!listener.tokens.get(i + 1).contentEquals("}") && forLoop) {
					for (int y = 0; y < indent; y++) {
						translation += (" ");
					}
				} else if (forLoop) {
					for (int y = 0; y < indent - 1; y++) {
						translation += (" ");
					}
				}
			} else if (listener.tokens.get(i).contentEquals("{") && !arrayInit) {
				translation += ("\n");
				indent++;
				for (int y = 0; y < indent; y++) {
					translation += ("    ");
				}
			} else if (listener.tokens.get(i).contentEquals("}") && !listener.tokens.get(i + 1).contentEquals(";")) {
				translation += ("\n");
				indent--;
				if (!listener.tokens.get(i + 1).contentEquals("}")) {
					for (int y = 0; y < indent; y++) {
						translation += ("    ");
					}
				} else {
					for (int y = 0; y < indent - 1; y++) {
						translation += ("    ");
					}
				}
			}
		}


		translation += (listener.tokens.get(listener.tokens.size() - 1));
		translation = translation.replace("break" , "break");
		translation = translation.replace("System.out.println", "Console.WriteLine");
		translation = translation.replace("System.out.print", "Console.WriteLine");
		translation = translation.replace("main", "Main");
		translation = translation.replaceAll("implements", ":");
		translation = translation.replaceAll("extends", ":");
		translation = translation.replace(".length()", ".Length");
		translation = translation.replace(".length", ".Length");
		translation = translation.replace(".charAt(i)", "[i]");
		translation = translation.replaceAll("Scanner.*.*;", "");
		translation = translation.replaceAll("=.*nextInt().*;", "= Convert.ToInt32(Console.ReadLine());");
		translation = translation.replaceAll("=.*nextDouble().*;", "= Convert.ToDouble(Console.ReadLine());");
		translation = translation.replaceAll("=.*nextShort().*;", "= (short)Convert.ToInt32(Console.ReadLine());");
		translation = translation.replaceAll("=.*nextLong().*;", "= (long)Convert.ToInt32(Console.ReadLine());");
		translation = translation.replaceAll("=.*nextLine().*;", "= Console.ReadLine();");
		translation = translation.replaceAll("=.*nextFloat().*;", "= float.Parse(Console.ReadLine());");
		translation = translation.replaceAll("=.*nextChar().*;", "= Console.ReadLine()[0];");
		translation = translation.replaceAll("\\]\\[", ",");
		translation = translation.replaceAll("boolean", "bool");
		translation = translation.replaceAll("=.*nextBoolean().*;", "= bool.Parse(Console.ReadLine());");
		translation = translation.replaceAll("=.*nextByte().*;", "= byte.Parse(Console.ReadLine());");
		translation = translation.replaceAll("\\] \\[", ",");
		cTextArea.setText(translation);
		translation = "";
		System.out.println("Translation Sucessful");
		return success;
	}
}
