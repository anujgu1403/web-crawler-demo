package com.ge.crawler.webcrawler.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.ge.crawler.webcrawler.thread.WebCrawlerThread;

/**
 * @author: anuj kumar
 * 
 *          This is the main thread processor class which used to start web
 *          crawler process
 * 
 */
public class WebCrawlerProcessor {

	/**
	 * main method to start web crawler thread
	 * 
	 * @param String[]
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		int maxThreads = 5;
		String searchKeyword = "page-50";
		String jsonFilePath = "C:\\web\\internet.json";

		ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
		for (int i = 0; i < maxThreads; i++) {
			WebCrawlerThread webCThread = new WebCrawlerThread(searchKeyword, jsonFilePath);
			executor.submit(webCThread);
			Thread.sleep(100);
		}
		
		// shut down the executor service here
		executor.shutdown();
	}
}
