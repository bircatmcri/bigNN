package edu.mfldclin.mcrf.deepsparktext.tools;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.StemmingPreprocessor;

public class ExtStemmPreProcessing extends StemmingPreprocessor{
	
	
 	
	
	
	
	public  ExtStemmPreProcessing() {
		super();
	}
	
	
	@Override	 
     public String preProcess(String token) {
         token = token.toLowerCase();
         String base = super.preProcess(token);
        
         base = base.replaceAll("\\d+.*", "");
        
         /*System.out.println(base); 
         System.out.println("****");*/
         return base;
     }
	

}
