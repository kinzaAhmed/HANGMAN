import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



public class Client {
    PrintWriter printWriter;
    public static void main(String[] args) throws UnknownHostException ,IOException
    {
        String name =args[0];
        int score=0;
        boolean playingGameC = true;
        Socket  socket = new Socket("localhost",8080);
     
        
        //send client name to the server
        PrintWriter clientNameR =new PrintWriter(socket.getOutputStream(),true);
        clientNameR.println(name);
        
        System.out.println("Connected Successfully..");
        
        
        
        PrintWriter printWriter =new PrintWriter(socket.getOutputStream(),true);
        //BufferedReader bufferedReader = new java.io.BufferedReader(new InputStreamReader(System.in));
        BufferedReader bufferedReaderR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        while (playingGameC)
        {
            System.out.println("Response From Server..");
            String guessWordR = bufferedReaderR.readLine();
            int numTries = Integer.parseInt(guessWordR);
            
            //Initialize guess word. In the beginning the guess word is ---. After each input the guessword changes
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < numTries; i++)
            {
                sb.append("-");
            }
            String prevWord = sb.toString();
            System.out.println(prevWord);
            // input will check for repetitive characters
            String input = prevWord;
            
            // check for successful guess or not
            boolean guessSuccessful=false;
            
            while (numTries!=0 && !guessSuccessful)
            {
                System.out.format("You have %d number of tries left\n", numTries);
                System.out.println("Enter a character :" );
                
                //Input Character
                Scanner scanner=new Scanner(System.in);
                
                //char c =scanner.next().charAt(0);
                String c=scanner.next().toLowerCase();
                if (c.equals("-"))
                {
                    numTries=0;
                    printWriter.println(c);
                    continue;
                }
                // Check if the input is a Character or a Word
                if (c.length()==1)
                {//If input character match any previous entry then notify and reask the User
                    if (input.indexOf(c)!=-1)
                    {
                        System.out.println("Hey! you have already entered this letter. Try another");
                        continue;
                    }
                    //add character to input string
                    input+=c;
                }
                printWriter.println(c);
                String readerInput = bufferedReaderR.readLine();
                
                //Compare the current guess word with previous one to check if the guess was ok or not
                System.out.println(readerInput);
                if (readerInput.equals(prevWord))
                {
                    numTries--;
                }
                else
                {
                    prevWord=readerInput;
                }
                
                
                //Compare any - in the guess  word sent by server. Any - means the input doesnt match the guess word yet.
                int checkDash = readerInput.indexOf('-');
                if (checkDash==-1)
                {
                    guessSuccessful=true;
                    score++;
                    System.out.println("Yahoo! you have won & your score is "+score);
                    
                }
            }
            if(!guessSuccessful)
            {
                score--;
                System.out.println("Alas! You have lost & your score is "+score);
                
            }
            
            System.out.println("Do you like to play again?(Y/N)");
            //Input Choice to Continue
            Scanner playAgain=new Scanner(System.in);
            char playAgainC =playAgain.next().charAt(0);
            //Inform Server about the option to continue or not
            PrintWriter toContinueC =new PrintWriter(socket.getOutputStream(),true);
            toContinueC.println(playAgainC);
            //if User wants to leave
            if (playAgainC=='N' || playAgainC=='n')
            {
                playingGameC=false;
                System.out.println("Bye! See you soon. Your score is "+score);
            }
        }
    }
}