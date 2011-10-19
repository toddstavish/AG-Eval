package com.linkage.csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.graphdb.LinkageContextManager;
import com.linkage.vo.LinkageInfoVo;


public class CsvReader {
	
	static Logger logger;
	static{
		logger = LoggerFactory.getLogger(CsvReader.class);
	}
	
	private BufferedReader br = null;
	
	public CsvReader(String fileName) throws FileNotFoundException{
		logger.info("Opening file: {}", fileName);
		br = new BufferedReader(new FileReader(fileName));
	}
	
	public String[] readLine() throws IOException {
		String s = br.readLine();
		if (s != null){
			return parseDataRow(s);
		}
		else
			return null;
	}
	
	public static String[] readParticularRow(int rowNumber) throws IOException {		
		long startTime = System.currentTimeMillis();
		String row = null;
		RandomAccessFile accessFile = null;
		try {
			//accessFile = LinkageContextListener.getRandomAccessFile();
			accessFile = LinkageContextManager.getRaf();
			accessFile.seek((rowNumber-1)*4371);
			row = accessFile.readLine();
			//logger.info("Row: {}", row);
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		logger.trace("Processing time readParticularRow = " + (endTime-startTime) + " milliseconds");
		return parseAtlasData(row);
	}
	
	
	
	public String[] readAtlasLine() throws IOException {
		String s = br.readLine();
		if (s != null){
			return parseAtlasData(s);
		}
		else
			return null;
	}
	
	public String readDunsNumber() throws IOException {
		String s = br.readLine();
		if (s != null){
			return s;
		}
		else
			return null;
	}
	
	public void close() {
		try {
			logger.info("Closing file.");
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private static String[] parseDataRow(String dataRow) {
		String[] data = dataRow.split("\\|");
		return data;
	}
	
	private static String[] parseAtlasData(String dataRow) {
				
		String[] data = new String[13];
		//duns number 4( sl.no number - mapped to layout)
		data[0] = dataRow.substring(20, 31);
		//bus name  5
		data[1] = dataRow.substring(31, 151);
		//global hier code 117
		data[2] = dataRow.substring(2893, 2895);		
		// phys address street line1 - 13
		data[3] = dataRow.substring(992, 1055);
		// phys address street line2 - 14
		data[4] = dataRow.substring(1055, 1119);
		//phys address post code - 21
		data[5] = dataRow.substring(1288, 1304);
		//phys address post town - 15
		data[6] = dataRow.substring(1119, 1169);
		// wbCtry - 22
		data[7] = dataRow.substring(1304, 1354);
		//global ultimate duns number - 112
		data[8] = dataRow.substring(2864, 2875);
		//published owner name 109
		data[9] = dataRow.substring(2839, 2850);
		// trading style 1
		data[10] = dataRow.substring(151, 271);
		// trading style 2
		data[11] = dataRow.substring(271, 391);		
		//operating status code
		data[12] = dataRow.substring(2761, 2762);
		
		return data;
	}
	
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//CsvReader in = new CsvReader("E:\\DnB\\content\\atlas.1k.txt");
		String data[] = CsvReader.readParticularRow(2);
		
			//System.out.println(data[0] + "     " +  data[1] + "     " + data[2] + "     " + data[3] + "     " + data[4] + "     " + data[5] + "     " + data[6]);
			
			LinkageInfoVo inofVo = new LinkageInfoVo(data, false);			
			System.out.println(inofVo.toString());
					
		
	}

}
