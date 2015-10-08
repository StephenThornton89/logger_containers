package org.aitek.collections.core;

import org.aitek.collections.gui.Main;
import org.aitek.collections.gui.StatsPanel;
import org.aitek.collections.utils.Constants;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListSample extends CollectionSample implements PropertyChangeListener {

	private Stack<Integer> stack;
	private CopyOnWriteArrayList<Integer> copyOnWriteArrayList;
	private Vector<Integer> vector;
	private long[] times;
	private Task task;

	public ListSample(StatsPanel statsPanel, Main main) {

		super(statsPanel, main);
		COLLECTION_TYPES = 3;
		times = new long[COLLECTION_TYPES];
		stack = new Stack<>();
		copyOnWriteArrayList = new CopyOnWriteArrayList<Integer>();
		vector = new Vector<Integer>(Constants.COLLECTION_MAX_SIZE * 1000);

	}

	public HashSet<OperationType> getSupportedOperations() {

		HashSet<OperationType> set = new HashSet<OperationType>();

		set.add(OperationType.POPULATE);
		set.add(OperationType.INSERT);
		set.add(OperationType.REMOVE);
		set.add(OperationType.SEARCH);
		set.add(OperationType.ITERATE);
		set.add(OperationType.SORT);

		return set;
	}

	public void execute(OperationType operation) {

		this.currentOperation = operation;

		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		statusBar.updateProgressBar(task.getProgress());
	}

	private class Task extends SwingWorker<Void, Void> {

		private double mult;

		@Override
		public Void doInBackground() {

			mult = 100d / iterations;
			switch (currentOperation) {
				case POPULATE:
					times = fillLists();
					statsPanel.setTimes("Populating", times);
				break;
				case INSERT:
					times = insertIntoLists();
					statsPanel.setTimes("Inserting new elements", times);
				break;
				case REMOVE:
					times = removeFromLists();
					statsPanel.setTimes("Removing existing elements", times);
				break;
				case SEARCH:
					times = searchLists();
					statsPanel.setTimes("Searching existing elements", times);
				break;
				case ITERATE:
					times = iterateOnLists();
					statsPanel.setTimes("Iterating elements", times);
				break;
				case SORT:
					times = sortLists();
					statsPanel.setTimes("Sorting elements", times);
				break;
			}

			return null;
		}

		@Override
		public void done() {

			main.setButtonsState();
			main.setReady();
		}

		private long[] fillLists() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Filling list with " + getListFormattedSize() + " elements...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				times = populateList();
				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			return times;
		}

		private long[] populateList() {

			long times[] = new long[COLLECTION_TYPES];
			stack.clear();
			vector.clear();
			copyOnWriteArrayList.clear();

			int[] toBeInserted = new int[listSize];
			for (int j = 0; j < getListSize(); j++) {
				toBeInserted[j] = (int) (Math.random() * listSize);
			}

			long startingTime = System.nanoTime();
			for (int j = 0; j < getListSize(); j++) {
				stack.add(toBeInserted[j]);
			}
			times[0] += System.nanoTime() - startingTime;

			startingTime = System.nanoTime();
			for (int j = 0; j < getListSize(); j++) {
				copyOnWriteArrayList.add(toBeInserted[j]);
			}
			times[1] += System.nanoTime() - startingTime;

			startingTime = System.nanoTime();
			for (int j = 0; j < getListSize(); j++) {
				vector.add(toBeInserted[j]);
			}
			times[2] += System.nanoTime() - startingTime;

			return times;
		}

		private long[] insertIntoLists() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Inserting elements into list...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				long startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					stack.add(stack.size() / 2, 0);
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					copyOnWriteArrayList.add(copyOnWriteArrayList.size() / 2, 0);
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					vector.add(vector.size() / 2, 0);
				times[2] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			return times;
		}

		private long[] removeFromLists() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Removing elements from list...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				long startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++)
					stack.remove(0);
				for (int j = 0; j < 10; j++)
					stack.remove(stack.size() / 2);
				for (int j = 0; j < 10; j++)
					stack.remove(stack.size()-1);
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++)
					copyOnWriteArrayList.remove(0);
				for (int j = 0; j < 10; j++)
					copyOnWriteArrayList.remove(copyOnWriteArrayList.size() / 2);
				for (int j = 0; j < 10; j++)
					copyOnWriteArrayList.remove(copyOnWriteArrayList.size()-1);
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++)
					vector.remove(0);
				for (int j = 0; j < 10; j++)
					vector.remove(vector.size() / 2);
				for (int j = 0; j < 10; j++)
					vector.remove(vector.size()-1);
				times[2] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}
			System.out.println(stack.size());
			return times;
		}

		private long[] searchLists() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Searching elements in list...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				long startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++) {
					stack.get(0);
					stack.get(stack.size() / 2);
					stack.get(stack.size() - 1);
				}
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++) {
					copyOnWriteArrayList.get(0);
					copyOnWriteArrayList.get(copyOnWriteArrayList.size() / 2);
					copyOnWriteArrayList.get(copyOnWriteArrayList.size() - 1);
				}
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 10; j++) {
					vector.get(0);
					vector.get(vector.size() / 2);
					vector.get(vector.size() - 1);
				}
				times[2] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			return times;
		}

		private long[] sortLists() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Sorting elements...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				populateList();

				long startingTime = System.nanoTime();
				Collections.sort(stack);
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				Collections.sort(copyOnWriteArrayList);
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				Collections.sort(vector);
				times[2] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			return times;
		}

		private long[] iterateOnLists() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Iterating on elements...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				long startingTime = System.nanoTime();
				Iterator<Integer> iterator = stack.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				iterator = copyOnWriteArrayList.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				iterator = vector.iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[2] += System.nanoTime() - startingTime;
				setProgress((int) (z * mult));
			}

			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			setProgress(100);

			return times;
		}

	}

	@Override
	public boolean isPopulated() {

		return vector.size() > 0;
	}

}
