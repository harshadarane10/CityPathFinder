package com.harshadaproject.citypathfinder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;  

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
 
@RestController  

public class CityPathFinderController {
	HashMap<String,Set<String>> cityPathMap= new HashMap<>();
	Queue<String> cityqueue = new LinkedList<String>();
	HashSet<String> visitedCities = new HashSet<>();
	Logger logger = LoggerFactory.getLogger(CityPathFinderController.class);
	private String CONNECTED = "yes";
	private String NOT_CONNECTED = "no";

	
	public void printCityMap() {		
		logger.trace("Display CityMap : ");
		for(String c : cityPathMap.keySet()) {			
			logger.trace("City "+c+"->"+cityPathMap.get(c).toString());
		}		
	}
	
	public boolean buildCityMap() {
		
		try {
			
			String fileName = "city.txt";
	        ClassLoader classLoader = new CityPathFinderController().getClass().getClassLoader();
	 
	        logger.info("File Name : " + fileName);
	        File file = null;
	        if(classLoader.getResource(fileName) != null)
	        	file = new File(classLoader.getResource(fileName).getFile());        
	        
	             
	        String content = null;
	        
	        if (file.exists()) {
	        	//Read File Content
	        	content = new String(Files.readAllBytes(file.toPath()));	        
	        	String[] slines = content.split("\\n");        
	        	String city1 = "";
	        	String city2="";
	        
	        	for(String s:slines) {
	        		//logger.trace(s);
	        		if(s.trim() != "") {
	        			String[] cities = s.split(",");	  
		        	
	        			city1 =cities[0].trim();
	        			city2 =cities[1].trim();
	        			//logger.trace("Source "+city1+"->"+"Dest"+city2);   		        	
		        	
	        			cityPathMap.putIfAbsent(city1, new HashSet<String>());
	        			cityPathMap.get(city1).add(city2);		        	
	        			//logger.trace("Entry added "+city1+ ":"+city2);
		        	
	        			cityPathMap.putIfAbsent(city2, new HashSet<String>());
	        			cityPathMap.get(city2).add(city1);
	        			//logger.trace("Entry added "+city2+ ":"+city1);
	        		}
	        	}
	       }else {
	    	   logger.error("File does not exist");
	    	   return false;
	       }
		
	    }catch(IOException e) {
			e.printStackTrace();	
			logger.error("IOException "+e);
		}catch(Exception e) {
			e.printStackTrace();	
			logger.error("Exception "+e);
		}
		
		return true;
	}
	
	public String searchCityMap(String source,String Destination) {
		String result=NOT_CONNECTED;		
		
		cityqueue.add(source);		
		while(!cityqueue.isEmpty()) {
			String searchCity = cityqueue.poll();			
			// Check all connected cities 
			// if match with dest then return yes
			// if not in visited cities then add in queue
			logger.trace("searchCity["+searchCity+"]");
			
			Set<String> cityset = cityPathMap.get(searchCity);
			
			logger.trace("cityset"+cityset);
			
			
			if(cityset!= null) {
				for(String c:cityset) {	
					logger.trace("connected city =["+c+": Dest=["+Destination+"]");
					if(c.equalsIgnoreCase(Destination)) {
						return CONNECTED;
					}
					else {
						if (!visitedCities.contains(c)) {
							cityqueue.add(c);
						}
					}
				}//End of for			
			}//End of null check
			
			visitedCities.add(searchCity);
		}//End of while
		
		return result;
		
	}
	
	@RequestMapping("/connect")  
	public String isCitiesConnected(@RequestParam(value="origin" ,required = false) String source,@RequestParam(value="destination", required = false) String dest)   
	{  
		String result = NOT_CONNECTED;
		
		if (!buildCityMap())
			return NOT_CONNECTED;
			
		printCityMap();
		
		if(source == null || dest == null) {
			return NOT_CONNECTED;
		}
		
		result = searchCityMap(source.trim(),dest.trim());
		logger.trace("result=["+result);
		
		return result;  
	}  
}
