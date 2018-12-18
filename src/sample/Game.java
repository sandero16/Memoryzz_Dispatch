package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements Serializable {
    private int gameId;
    private boolean end;
    private ArrayList<String>players;
    private ArrayList<Integer>scores;
    private ArrayList<Integer>keuzes;
    private ArrayList<Integer>plaatsen;
    // private int plaats1;
    //private int plaats2;
    private String beurt;
    private int[][] matrix;
    private ArrayList<ArrayList<Integer>> reedsGezet;
    private int[] gegokt;
    private boolean initialized;
    private ArrayList<Boolean>update;
    private ArrayList<Boolean> viewerupdate;

    private int aantalSpeler;

    public Game(){

    }
    public ArrayList<String> getPlayers(){
        return players;
    }
    public void startGame(ArrayList<String> gamePlayers){
        players=new ArrayList<>();
        for (String player: gamePlayers) {
            players.add(player);
        }
        scores=new ArrayList<>();
        keuzes=new ArrayList<>();
        plaatsen=new ArrayList<>();
        update=new ArrayList<>();
        viewerupdate=new ArrayList<>();
        end=false;
        reedsGezet=new ArrayList<>();
        for(int i=0;i<gamePlayers.size();i++) {
            update.add(false);
        }
        initialized=false;
    }
    public void generateMatrix(){

        System.out.println("generating"+ " beurt: "+beurt);
        for (String sessionToken: players) {
            System.out.print(sessionToken+" ");
        }
        System.out.println();
        if(!initialized) {
            System.out.println("for loop");
            for(int i=0;i<players.size();i++){
                System.out.println("i :"+players.get(i));
            }
            System.out.println("foreach");
            for (String sessionToken:players) {
                System.out.println("player: "+sessionToken);
            }

            for(int i=0;i<players.size();i++){
                scores.add(0);
            }


            gegokt=new int[2];

            beurt=players.get(0);
            initialized=true;
            List<Integer> solution = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                solution.add(i);
                solution.add(i);
            }
            Collections.shuffle(solution);
            for (int i : solution) {
                System.out.print(i + " ");
            }
            matrix = new int[4][4];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrix[i][j] = solution.get(i * 4 + j);
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }
        }

    }

    public synchronized int getFromMatrix(int i){
        System.out.println("value= "+i);
        i--;
        int row=i/4;
        int column=i%4;
        System.out.println("rij "+row+ "column: "+column);
        gegokt[0]=i;
        gegokt[1]=matrix[row][column];

        System.out.println("//////////////////////////////");
        System.out.println(gegokt[1]);
        System.out.println("//////////////////////////////");
        for(int j=0;j<viewerupdate.size();j++){
            viewerupdate.set(j,true);
        }
        //notifyAll();
        /*for(int j=0;j<update.size();j++){
            update.set(j,true);
        }*/
        keuzes.add(matrix[row][column]);
        plaatsen.add(i);
        /*if(keuze1==-1){
            keuze1=matrix[row][column];
            plaats1=i;
        }
        else{
            keuze2=matrix[row][column];
            plaats2=i;
        }*/
        if(keuzes.size()==2){
            if(keuzes.get(0)==keuzes.get(1)){
                scores.set(players.indexOf(beurt),scores.get(players.indexOf(beurt))+1);

                ArrayList<Integer>temp=new ArrayList<>();
                temp.add(keuzes.get(0));
                temp.add(plaatsen.get(0));
                temp.add(plaatsen.get(1));
                reedsGezet.add(temp);

                if(reedsGezet.size()==8)end=true;

                for (Integer k:scores) {
                    System.out.println("score: "+k);
                }
            }
            keuzes.clear();
            plaatsen.clear();

        }
        return matrix[row][column];
    }
    public void changeBeurt(){
        //beveiliging
        int index=players.indexOf(beurt);
        /*if(keuze1==keuze2){
            scores.set(index,scores.get(index)+1);
            System.out.println("#############");
                for (Integer i:scores) {
                    System.out.println("score: "+i);
                }
            System.out.println("##############");
            ArrayList<Integer>temp=new ArrayList<>();
            temp.add(keuze1);
            temp.add(plaats1);
            temp.add(plaats2);
            reedsGezet.add(temp);
            if(reedsGezet.size()==8)end=true;
        }*/

        if(index==(players.size()-1)){
            beurt=players.get(0);
        }
        else{
            beurt=players.get(index+1);
        }

    }
    public String checkBeurt(){
        // System.out.println("checkbeurt "+beurt);
        return beurt;

    }
    public boolean getEnd(){
        return end;
    }
    public int getIndex(String sessionToken){
        return players.indexOf(sessionToken);
    }
    public synchronized int[] getTegenspelerGok(String sessionToken){
        try{
            wait();
            //TODO hier chekken of moet migreren
            return gegokt;
        }
        catch (Exception e){
            System.out.println("waiting failed");
            e.printStackTrace();
            System.out.println(e);
            return null;
        }
         /*int index=players.indexOf(sessionToken);
       if(update.get(index)) {
            update.set(index,false);
            return gegokt;
        }
        else{
            return null;
        }*/
    }
    public synchronized int[] getGok(int viewerid){
        //if(viewerupdate.get(viewerid)) {
        try {
            //viewerupdate.set(viewerid, false);
            System.out.println("waiting");
            //
            for (int i:gegokt) {
                System.out.println("getGOk: "+i);
            }
            wait();
            System.out.println("gotit");
            return gegokt;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        // }
        return null;
    }
    public int[] getInhaalGok(int viewerId){
        return gegokt;
    }
    public int getViewerId(){
        int viewerId=viewerupdate.size();
        viewerupdate.add(false);
        return viewerId;
    }
    public int getResult(String sessionToken){
        int index=players.indexOf(sessionToken);
        int spelerScore = scores.get(index);
        int aantalSpelers = players.size();
        int hogerAantalSpelers=0;
        int gelijkAantalSpelers=0;
        int resultaat = 0;
        for(int i = 0; i<scores.size();i++){
            if(i!=index) {
                if (scores.get(i) > spelerScore)
                    hogerAantalSpelers++;
                else if (scores.get(i) == spelerScore)
                    gelijkAantalSpelers++;
            }
        }
        resultaat = hogerAantalSpelers + 1;

        if(gelijkAantalSpelers>0) resultaat += 10; //resultaat groter dan 10 doorsturen wil zeggen dat plaats gedeeld is
        return resultaat;
    }
    public int getScore(String sessionToken){
        return scores.get(players.indexOf(sessionToken));
    }
    public ArrayList<ArrayList<Integer>> getReedsGezet(){
        return reedsGezet;
    }
    public synchronized void geefNotify(){
        notifyAll();
    }
    public synchronized void notifyMigration(int migratieadares){
        gegokt[0]=migratieadares;
        notifyAll();
    }
    public int getOntvangenRankingPunten(String sessionToken){
        int aantalSpelers = players.size();
        int resultaat = getResult(sessionToken);
        if(resultaat>10) resultaat -= 10;
        return aantalSpelers + 1 - resultaat;
    }
    public int getAantalSpelers(){
        return players.size();
    }

}
