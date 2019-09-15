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
	 */
	public static void main(String[] args) throws InterruptedException {
		long startTime = System.currentTimeMillis();
		int maxThreads = 5;
		String searchKeyword = "page-50";
		String jsonFilePath = "C:\\web\\internet.json";

		System.out.println("Starting web crawling");
		/*
		 * for (int i = 0; i < maxThreads; i++) { WebCrawlerThread webCThread = new
		 * WebCrawlerThread(searchKeyword, jsonFilePath);
		 * 
		 * //To start the thread webCThread.start();
		 * 
		 * //To let thread complete one by one completely webCThread.join(); }
		 */
		ExecutorService service = Executors.newFixedThreadPool(maxThreads);
		for (int i = 0; i < maxThreads; i++) {
			WebCrawlerThread webCThread = new WebCrawlerThread(searchKeyword, jsonFilePath);
			service.execute(webCThread);
			Thread.sleep(100);

		}
		service.shutdown();
		if (service.isShutdown()) {
			System.out.println("Process time: " + (System.currentTimeMillis() - startTime) + " ms.");
		}
	}
}
