package com.harshadaproject.citypathfinder;

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

	
	public void printCityMap() {		
		System.out.println("Display CityMap : ");
		for(String c : cityPathMap.keySet()) {
			System.out.println("City "+c+"->"+cityPathMap.get(c).toString());
		}		
	}
	
	public void buildCityMap() {
		
		try {
			
			String fileName = "city1.txt";
	        ClassLoader classLoader = new CityPathFinderController().getClass().getClassLoader();
	 
	        System.out.println("File Name : " + fileName);
	        
	        File file = new File(classLoader.getResource(fileName).getFile());
	        
	        System.out.println("File Object : " + file);
	         
	        //File is found
	        System.out.println("File Found : " + file.exists());
	         
	        //Read File Content
	        String content = new String(Files.readAllBytes(file.toPath()));
	        
	        String[] slines = content.split("\\n");
	        
	        
	        
	        String city1 = "";
	        String city2="";
	        
	        for(String s:slines) {
	        	System.out.println(s);
	        	if(s.trim() != "") {
		        	String[] cities = s.split(",");	  
		        	
		        	city1 =cities[0].trim();
		        	city2 =cities[1].trim();
		        	System.out.println("Source "+city1+"->"+"Dest"+city2);    	
		        	
		        	
		        	cityPathMap.putIfAbsent(city1, new HashSet<String>());
		        	cityPathMap.get(city1).add(city2);
		        	
		        	System.out.println("Entry added "+city1+ ":"+city2);
		        	
		        	cityPathMap.putIfAbsent(city2, new HashSet<String>());
		        	cityPathMap.get(city2).add(city1);
		        	System.out.println("Entry added "+city2+ ":"+city1);
	        	}
	        }
		}catch(IOException e) {
			e.printStackTrace();			
		}catch(Exception e) {
			e.printStackTrace();			
		}
		
	}
	
	public String searchCityMap(String source,String Destination) {
		String result="no";		
		
		cityqueue.add(source);		
		while(!cityqueue.isEmpty()) {
			String searchCity = cityqueue.poll();			
			// Check all connected cities 
			// if match with dest then return yes
			// if not in visited cities then add in queue
			Set<String> cityset = cityPathMap.get(searchCity);
			
			
			if(cityset!= null) {
				for(String c:cityset) {					
					if(c.equalsIgnoreCase(Destination)) {
						return "yes";
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
	/*
	 * 	Boston, New York
		Philadelphia, New York
		Newark, Boston
		Trenton, Albany
	 */
	/*
	http://localhost:8080/connect?origin="Boston"&destination="New York"
	http://localhost:8080/connect?origin=Boston&destination=Philadelphia
	http://localhost:8080/connect?origin=Boston&destination=Albany
	http://localhost:8080/connect?origin=Boston&destination=Trenton
    */
	@RequestMapping("/connect")  
	public String isCitiesConnected(@RequestParam(value="origin" ,required = false) String source,@RequestParam(value="destination", required = false) String dest)   
	{  
		String result = "no";
		
		buildCityMap();
		printCityMap();
		
		if(source == null || dest == null) {
			return "no";
		}
		
		result = searchCityMap(source.trim(),dest.trim());
		
		return result;  
	}  
}
