package br.ufrn.lets.stacktraceminer.stackoverflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.ufrn.lets.stacktraceminer.core.StacktraceManager;

/**
 * Implements the methods necessary to mining stack traces by the Dump file on XLS format.
 * This file contains the posts that were exported from the Stackoverflow with help of a query service available on the Stackexchange.
 * <br/>Reference: http://data.stackexchange.com/stackoverflow/query/310472/what-are-the-posts-that-have-exception-tag
 * 
* @author Teresa Fernandes
* */
public class StackoverflowDumpXlsManager  extends StacktraceManager{

	private static String csvFilePath = "resources/QueryResultsWithTagException.xlsx";
	
	private static int idIndex = 0;
	private static int typeIndex = 1;
	private static int bodyIndex = 4;
	private static int tagsIndex = 5;
	private static int creationDateIndex = 2;
	
	
	/** Singleton pattern*/
	private StackoverflowDumpXlsManager() {	}
	public static synchronized StackoverflowDumpXlsManager getInstance() {
        if (instance == null) 
            instance = new StackoverflowDumpXlsManager();
        return (StackoverflowDumpXlsManager) instance;
    }	

	/**
	 * Read the excel file and extract the posts from Stackoverflow that corresponds que correspondes to  the stack trace pattern.
	 * */
	@SuppressWarnings("resource")
	@Override
	public void startExtraction() {
		FileInputStream file = null ;
		try {
	        file = new FileInputStream(new File(csvFilePath));
	        XSSFWorkbook workbook = new XSSFWorkbook(file);

	        //Get first/desired sheet from the workbook
	        XSSFSheet sheet = workbook.getSheetAt(0);
	        System.out.println("Total of posts: "+(sheet.getPhysicalNumberOfRows()-1));
	        
	        //Iterate through each rows one by one
	        Iterator<Row> rowIterator = sheet.iterator();
	        rowIterator.next();
	        int count = 1;
	        while (rowIterator.hasNext()){
	            Row row = rowIterator.next();
	            
                if(row.getPhysicalNumberOfCells() > tagsIndex){
		            boolean isQuestion = !getStringCellValueByIndex(row,typeIndex).isEmpty() && getStringCellValueByIndex(row,typeIndex).equals("Question"); 
		            identifyAndLoadStacktraces(getStringCellValueByIndex(row,bodyIndex), isQuestion ? getStringCellValueByIndex(row,idIndex) : null, !isQuestion ? getStringCellValueByIndex(row,idIndex) : null, getStringCellValueByIndex(row,tagsIndex),getStringCellValueByIndex(row,creationDateIndex),null);
                }
                System.out.println("Row "+count++);
                
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	    	if(file!=null){
		        try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		        System.out.println("Stacktraces extracted: "+(getStacktraces()!=null? getStacktraces().size() : 0));
		        saveStacktraces();
	    	}
	    }
	}

	/**
	 * Returns the cell (excel) value as a String
	 * @param row
	 * @param cellIndex
	 * @return
	 * */
	private String getStringCellValueByIndex(Row row, int cellIndex){
		Cell cell = row.getCell(cellIndex);
		String cellValue = null;
		
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING :
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC :
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			default:
				cellValue = cell.getStringCellValue();
				break;
		}
		return cellValue;
	}
	
}
