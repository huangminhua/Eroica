package eroica.util.concurrent;

import java.util.Random;

import eroica.util.concurrent.HashSynchronizerGroup.HashSynchronizer;


public class HashSynchronizerGroupTest {
	public static void main(String[] args) {
		for (int a = 2; a <= 128; a++) {
			HashSynchronizerGroup sg = new HashSynchronizerGroup(a);
			int[] index = new int[a];
			for (int i = 0; i < 1024; i++) {
				HashSynchronizer s = sg.getSyner(String.valueOf(i + new Random().nextInt()));
				index[Integer.parseInt(s.getId())]++;
			}
			// System.out.println(Arrays.toString(index));
			int max = 0;
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < index.length; i++) {
				if (max < index[i])
					max = index[i];
				if (min > index[i])
					min = index[i];
			}
			System.out.println(a + "," + max + "," + min + "," + ((double) min / (double) max));
		}
	}
}
