package com.lab;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

public class Synchronizator {
	private AtomicBoolean monitor;
	private ExecutorService executor;

	public Synchronizator() {
		monitor = new AtomicBoolean(true);
		executor = Executors.newSingleThreadExecutor();
	}

	public void start() {
		Runnable task = () -> {
			while (monitor.get()) {
				try {
					synchronizuj();
					TimeUnit.SECONDS.sleep(7);
				} catch (InterruptedException e) {
					System.out.println("W¹tek monitoruj¹cy obudzony.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(task);
	}

	public void stop() {
		monitor.set(false);
		executor.shutdownNow();
	}

	private void synchronizuj() throws IOException {
		Path pathA = Paths.get("A");
		Path pathB = Paths.get("B");
		boolean isDirectoryA = Files.isDirectory(pathA);
		if (!isDirectoryA) {
			System.out.println("Œcie¿ka nie jest katalogiem");
		}
		boolean existsA = Files.exists(pathA);
		if (!existsA) {
			System.out.println("Katalog nie istnieje");
		}

		File dirA = pathA.toFile();
		File dirB = pathB.toFile();
		String[] listA = dirA.list();
		String[] listB = dirB.list();

		for (int i = 0; i < listA.length; i++) { //foreach?
			boolean kopiuj = true;
			String fileToCopy = listA[i];
			for (int b = 0; b < listB.length; b++) {
				if (fileToCopy.equals(listB[b])) {
					kopiuj = false;
					break;
				}
			}
			if(kopiuj) {
				Path src = Paths.get(pathA.toString(), fileToCopy);
				Path dst = Paths.get(pathB.toString(), fileToCopy);
				if (!Files.isDirectory(src)) {
					System.out.println("Kopiowanie pliku " + fileToCopy);
					Files.copy(src, dst);
				} else {
					System.out.println("Tworzenie katalogu " + fileToCopy);
					Files.createDirectory(dst);
				}
			}
		}
		listB = dirB.list(); //aktualizacja listy
		for(int i = 0;i < listB.length;i++) {
			boolean del = true;
			String fileToDelete = listB[i];
			for(int b=0;b<listA.length;b++) {
				if(listA[b].equals(fileToDelete)) {
					del=false;
					break;
				}
			}
			if(del) {
				Path file = Paths.get(pathB.toString(), fileToDelete);
				System.out.println("Usuwanie " + fileToDelete);
				Files.delete(file);
			}
		}
		listB = dirB.list();
	}

	public static void main(String[] args) {
		Synchronizator synch = new Synchronizator();
		synch.start();
		try {
			TimeUnit.MINUTES.sleep(1);
		} catch (InterruptedException e) {

		}
		synch.stop();
	}
}
