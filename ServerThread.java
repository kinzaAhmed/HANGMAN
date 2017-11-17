import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.*;
import java .*;


public class ServerThread extends Thread
{
    Socket socket;
    String guessWord;
    String guessWordCaseSens;
    char[] myWord;
    int numGuess;
    int tries;
    boolean gameOn;
    String clientID;
    int clientScore;
    boolean playingGameS;
    String checkToContinue;
    String checkToConStr;
    
    ServerThread(Socket socket)
    {
        this.socket=socket;
        //to get name from the client
        
        // pick random word from the word list
        guessWordCaseSens = pickWord();
        guessWord = guessWordCaseSens.toLowerCase();
        myWord = new char[guessWord.length()];
        numGuess = myWord.length;
        //System.out.println(guessWord);
        gameOn =true;
        clientScore=0;
        playingGameS=true;
        
    }


    public  void run()
    {
        
//       Get client name
        try {
            BufferedReader clientName = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientID = clientName.readLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        
        
        
        while (playingGameS)
        {
            PrintWriter printWriter;
            for (int i=0; i< numGuess; i++)
            {
                myWord[i]= '-';
            }
            tries= numGuess;
            try
            {
                String message = null;
                // Read Letters from Client
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                //Sending number of Guesses to make
                printWriter =new PrintWriter(socket.getOutputStream(),true);
                printWriter.println(tries);
                System.out.println(clientID +" is given word: "+guessWord);
                //message =bufferedReader.readLine();
                //Read incoming response from client
                //for(int i=0; i<tries; i++)
                boolean guessSuccessfulS=false;
                
                while(tries>0 && !guessSuccessfulS)
                {
                    message =bufferedReader.readLine();
                    //boolean result=charCompare(message);
                    if (message.equals("-"))
                    {
                        tries=0;
                        continue;
                    }
                    if(message.length()==1)
                    {
                        char c = message.charAt(0);
                        boolean result=charCompare(c);
                        
                    }
                    else
                    {
                        wordCompare(message);
                        
                    }
                    //At this point myWord should have the output from the Compare functions above.
                    System.out.println(clientID+" has entered: " +message+" and has "+ tries + " tries left");
                    if (checkWin())
                    {
                        guessSuccessfulS=true;
                        clientScore++;
                        System.out.println(clientID + " has won.Score: "+clientScore);
                    }
                    printWriter.println(myWord);
                    //System.out.println(tries);
//					if (tries>=1)
//					{
//					message =bufferedReader.readLine();
//					}

                }
                if(!guessSuccessfulS)
                {
                    clientScore--;
                    System.out.println(clientID +" has lost! Score: "+clientScore);
                }
//                bufferedReader.close();
//                printWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }            
                
                //read from Client whether to continue or abort.
                try {
                    BufferedReader toContinue = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    checkToContinue = toContinue.readLine();
                    checkToConStr=checkToContinue.toLowerCase();
                    
                    if (checkToConStr.equals("n"))
                    {
                        
                        playingGameS=false;
                        System.out.println(clientID +" has left the Game! Score: "+clientScore);
                        toContinue.close();
//                        printWriter.close();
                    }
                    // pick a new random word from the word list for the next game
                    if (checkToConStr.equals("y"))
                    {
                        guessWordCaseSens = pickWord();
                        guessWord = guessWordCaseSens.toLowerCase();
                        myWord = new char[guessWord.length()];
                        numGuess = myWord.length;
                    }
                    
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

        }
    }
	// a function to compare the current myWord array with the actual guessed word
        public boolean checkWin()
        {
            String myWordSt = new String(myWord);
            if (myWordSt.equals(guessWord))
                return true;
            else return false;
        }
        
        //picks random word
        public String pickWord ()
        {
            FileInputStream textFile;
            BufferedReader wordList;
            String PATH = "C:\\Users\\kinza\\Desktop\\networkprogramming\\Assignment1\\hangman\\src\\words.txt";
            // Creates a string array
            ArrayList<String> arrayWordList = new ArrayList<String>();
            
            // Read in the text file and randomize the words
            
            try {
                textFile = new FileInputStream(PATH);
                wordList = new BufferedReader(new InputStreamReader(textFile));
                String line = wordList.readLine();
                while (line != null) {
                    arrayWordList.add(line);
                    line = wordList.readLine();
                }
                textFile.close();
            }
            catch (Exception e) {
            }
            Random rand = new Random();
            int arrayIntNumber = rand.nextInt(arrayWordList.size());
            String returnWord = arrayWordList.get(arrayIntNumber);
            return returnWord;
        }
        
        
        public boolean charCompare(char x)
        {
            boolean compare = false;
            //String str = String.valueOf(compare);
            for(int i = 0; i<guessWord.length();i++)
                {
                    if(guessWord.charAt(i) == x)
                    {
                        myWord[i]=x;
                        compare = true;
                        //System.out.println(myWord[i]);
                    }
                }
                
                if(!compare)
                {
                    tries--;
                }
            return compare;
            
        }
        public void wordCompare(String x)
        {
            
            if (x.equals(guessWord)) {
                myWord=guessWord.toCharArray();
            }
            else {
                tries--;
            }
        }
}
        
                
