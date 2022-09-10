/* Fardowsa Douled
   12/02/2021
   CSE143
   TA: Effie Zheng
   Take Home Assignment #7
   In this program represents a 20 question game where one player
   chooses a secret object and the other player asks yes/no
   questions to try to idenfity the chosen object
 */
import java.util.*;
import java.io.*;

public class QuestionsGame {   
   private QuestionNode questionTree; 
   private Scanner console;
   
   // post: This constructor represents a single node of the tree 
   private static class QuestionNode {
      public String data;
      public QuestionNode left;
      public QuestionNode right;
      
      // post: sets a QuestionNode and sets it to null  
      public QuestionNode (String text) {
         this(text, null, null);
      }
   
      // post: makes a QuestionNode and sets the answers to the
      //       correct part of the tree
      public QuestionNode (String text, QuestionNode yesAnswer, QuestionNode noAnswer) {
         data = text;
         left = yesAnswer;
         right = noAnswer;
      }
   }


   // post: This constructor initializes a new QuestionsGame object 
   //       with a single leaf node representing the object “computer”
   public QuestionsGame() {
      questionTree = new QuestionNode("computer"); 
      console = new Scanner(System.in); 
   }
   
   // parameter: Scanner input - reads input linked to the file 
   // post: This method replaces the current tree by reading another tree from a file
   // and replaces the current tree with a new tree using the information in the file
   // This method also assumes that the file is legal and in standard format
   public void read(Scanner input) {
      questionTree = reader(input);
   }
   
   // parameter: Scanner input - reads input linked to the file
   // post: This helper method returns a new QuestionNode (question tree)
   private QuestionNode reader(Scanner input) {
      if (!input.nextLine().equals("A:")) {
         return new QuestionNode(input.nextLine(), reader(input), reader(input));
      } else {
         return new QuestionNode(input.nextLine());
      }
   }
    
   // parameter: PrintStream output - print output of current questions tree to the file 
   // pre: If the output is null this method throw new IllegalArgumentException
   // post: This method can be used to later play another game with the computer 
   // using questions from this an initial game
   // This ouput file is written in a legal and in standard format
   public void write(PrintStream output) {
      if (output == null) {
         throw new IllegalArgumentException(); 
      }
      writeTree(questionTree, output);
   }
   
   // post: this helper method that determines whether a specific node 
   // a repsonse Node /leaf Node
   private boolean reponseNode(QuestionNode answer) {
      return (answer.right == null || answer.left == null);
   }

   // parameter: QuestionNode questionTree - stores content of user input to file
   //             PrintStream output - print output of current questions tree to the file 
   // post: this helper method that write down the content of the current tree to
   // an input file
   private void writeTree(QuestionNode questionTree, PrintStream output) {
      if (reponseNode(questionTree)) {
         output.println("A:"); 
         output.println(questionTree.data);
      } else {
         output.println("Q:");
         output.println(questionTree.data);
         writeTree(questionTree.left, output);
         writeTree(questionTree.right, output); 
      }   
   }
   
   // post: This method uses the current tree to ask the user a series of 
   //       yes/no questions  until reaching an answer object to guess 
   //       correctly or false if it guesses incorrectly it expands 
   //       the tree to include their object and a new question 
   //       to distinguish their object from the others within the tree. 
   public void askQuestions() {
      questionTree = askQuestions(questionTree); 
   }
   
   //parameter: QuestionNode current - a question tree
   // post: This method uses the current tree to ask the user a series of 
   //       yes/no questions  until reaching an answer object to guess 
   //       If the computer wins the game, this method should print a message
   //       saying so 
   //       if not, it asks what object they were thinking of,  
   //       a question to distinguish that object from the player’s guess
   //       whether the player’s object is the yes or no answer for that question
   //return:This method returns a new question tree
   private QuestionNode askQuestions(QuestionNode current) {
      if (reponseNode(current)) {
         if (yesTo("Would your object happen to be " + current.data +"?")) {
            System.out.println("Great, I got it right!");
         } else {
            System.out.print("What is the name of your object? ");
            QuestionNode theAnswer = new QuestionNode(console.nextLine());
            System.out.println("Please give me a yes/no question that");
            System.out.println("distinguishes between your object");
            System.out.print("and mine--> "); 
            String theQuestion = console.nextLine(); 
            if (yesTo("And what is the answer for your object?")) {
               current = new QuestionNode(theQuestion, theAnswer, current); 
            } else {
               current = new QuestionNode(theQuestion, current, theAnswer); 
            }   
         }
      } else {
         if (yesTo(current.data)) {
            current.left = askQuestions(current.left);
         } else {
            current.right = askQuestions(current.right); 
         }   
      } 
      return current;
   }
     
   // post: This method asks the user a question, until the user types “y” or “n”
   //       in reponse it returns true if “y”, false if “n”      
   private boolean yesTo(String prompt) {
      System.out.print(prompt + " (y/n)? ");
      String response = console.nextLine().trim().toLowerCase();
      while (!response.equals("y") && !response.equals("n")) {
         System.out.println("Please answer y or n.");
         System.out.print(prompt + " (y/n)? ");
         response = console.nextLine().trim().toLowerCase();
      }
      return response.equals("y");
   }
}

