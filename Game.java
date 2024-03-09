package wit.comp1050;

import java.util.*;

public class Game {

    /*
    color key
    r-red o-orange y-yellow g-green
    b-blue v-violet w-white k-black
     */
    static char[] easyColors = {'r','y','g','b','v'};
    static char[] normalColors = {'r','o','y','g','b','v'};
    static char[] hardColors = {'r','o','y','g','b','v','w'};
    static char[] expertColors = {'r','o','y','g','b','v','w','k'};

    // player chooses whether the same color will appear more than once
    public static boolean allowDuplicates(String s){
        return s.equals("Yes");
    }

    // return the colors that correspond to the level
    public static char[] levelColors(String level){
        if(level.equals("Easy")){
            return easyColors;
        }
        else if(level.equals("Normal")){
            return normalColors;
        }
        else if(level.equals("Hard")){
            return hardColors;
        }
        else{
            return expertColors;
        }
    }

    // return the code length that corresponds to the level
    public static int codeLength(String level){
        if(level.equals("Easy")){
            return 4;
        }
        else if(level.equals("Normal")){
            return 4;
        }
        else if(level.equals("Hard")){
            return 5;
        }
        else{
            return 6;
        }
    }

    // number of different colors is referred to when generating the secret code
    public static int numColors(String level){
        if(level.equals("Easy")){
            return 5;
        }
        else if(level.equals("Normal")){
            return 6;
        }
        else if(level.equals("Hard")){
            return 7;
        }
        else{
            return 8;
        }
    }

    public static String generateCode(String level, boolean dups){
        String result="";
        Random codeGenerator = new Random();
        int length = codeLength(level);
        ArrayList colorList = new ArrayList(); //temp list of colors to randomly choose from
        int num;
        char[] c = levelColors(level);
        if(!dups){ //if no duplicate colors allowed
                for(int i=0; i<numColors(level); i++){
                    colorList.add(c[i]); //copy level colors into colorList
                }
                Collections.shuffle(colorList); //randomize order
                for (int i=0; i<length; i++) { //stops when result length is correct
                    num = codeGenerator.nextInt(colorList.size()); //random index in the list
                    result += colorList.get(num); //add it to the code
                    colorList.remove(num); //remove it from selection to avoid duplicates
                }
        }
        else{ //if duplicate colors are allowed
                for (int i = 0; i<length; i++) {
                    num = codeGenerator.nextInt(c.length); //pick a random color by index
                    result += c[num]; //add to result
                }
        }

        return result;
    }

    public static int guesses(String lvl){
        if(lvl.equals("Easy")){
            return 12;
        }
        else if(lvl.equals("Normal")){
            return 10;
        }
        else if(lvl.equals("Hard")){
            return 10;
        }
        else {
            return 8;
        }
    }

    //black hints indicate correct color in correct position
    public static int countBlackHints(String code, String guess){
        int blackHints=0;
        for (int i = 0; i<code.length(); i++) {
            if (code.charAt(i) == guess.charAt(i)) {
                blackHints++;
                if (blackHints == code.length()) {
                    System.out.println(winMessage());
                    System.exit(0);
                }

            }

        }
        return blackHints;
    }

    //white hints indicate correct color in wrong position
    public static int countWhiteHints(String code, String guess){
        int whiteHints=0;
        char[] secretCode = code.toCharArray();
        char[] playerGuess = guess.toCharArray();

        for (int i=0; i<code.length(); i++) {
            if (secretCode[i]==playerGuess[i]) { //blank out the correct guesses (by color and pos)
                secretCode[i]=' ';
                playerGuess[i]=' ';
            }
        }

        /*
        within the inner j loop, "black hints" will never be mistaken as white hints because the indices of those
        positions are blanked out and will be skipped
         */
        for (int i=0; i <code.length(); i++) { //checking secret position
            if (secretCode[i]==' ') { //was already guessed correctly
                continue;
            }
            for (int j=0;j<code.length(); j++) { //checking guess position
                if (playerGuess[j]==' ') { //was already guessed correctly
                    continue;
                }
                if (secretCode[i]==playerGuess[j]) {
                    whiteHints++;
                    secretCode[i]=' '; // blank out what has been checked already
                    playerGuess[j]=' ';
                }
            }
        }
        return whiteHints;
    }

    public static String loseMessage(){
        return "No more guesses left! Play again?";
    }

    public static String winMessage(){
        return "You win! Play again?";
    }



    public static void main(String args[]){
        Scanner input = new Scanner(System.in);
        System.out.println("Select a difficulty: Easy, Normal, Hard, Expert");
        String difficulty = input.nextLine();
        int allowedGuesses = guesses(difficulty);
        System.out.println("Allow duplicates? Yes/No");
        String dup = input.nextLine();
        String correctCode = generateCode(difficulty, allowDuplicates(dup));

        while (allowedGuesses>0) {
            System.out.println("Guess a " + codeLength(difficulty) + " colors long code of these colors: " + Arrays.toString(levelColors(difficulty)));
            String guess = input.nextLine();
            allowedGuesses--;


            System.out.println(countBlackHints(correctCode, guess) + " of your colors are the correct color in the right place.");
            System.out.println(countWhiteHints(correctCode, guess) + " of your colors are the correct color in the wrong place.");
            if(allowedGuesses>0){
                System.out.println("You have " + allowedGuesses + " guesses left.");
            }

        }
        if(allowedGuesses==0){
            System.out.println(loseMessage());
        }

    }
}
