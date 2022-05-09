package com.example.deepname.Utils;

public class Levenshtein {

    //Levenshtein distance (编辑距离)
    private static int getDistance(String s1,String s2){
        int m= (s1==null)?0:s1.length();
        int n= (s2==null)?0:s2.length();
        if(m==0){
            return n;
        }
        if(n==0){
            return m;
        }
        int[][] d=new int[m+1][n+1];


        for(int i=0;i<=m;i++){
            d[i][0]=i;
        }
        for(int i=1;i<=n;i++){
            d[0][i]=i;
        }
        for(int i=1;i<=m;i++){
            char s1_c=s1.charAt(i-1);
            for(int j=1;j<=n;j++){
                d[i][j]=Math.min(Math.min(d[i-1][j],d[i][j-1])+1,d[i-1][j-1]+((s1_c==s2.charAt(j-1))?0:1));
            }
        }
        return d[m][n];
    }

    public static float getSimilarity(String s1,String s2){
        if(s1==null||s2==null){
            if(s1==s2){
                return 1.0f;
            }
            return 0.0f;
        }
        float d=getDistance(s1,s2);
        return 1-(d/Math.max(s1.length(), s2.length()));
    }
}
