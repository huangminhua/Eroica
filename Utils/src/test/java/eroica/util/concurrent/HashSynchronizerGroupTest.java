package eroica.util.concurrent;

import java.util.Arrays;
import java.util.Random;

import eroica.util.concurrent.SynchronizerGroup.Synchornizer;

public class HashSynchronizerGroupTest {
	public static void main(String[] args) {
		SynchronizerGroup sg = new HashSynchronizerGroup();
		int[] index = new int[128];
		for (int i = 0; i < 4096; i++) {
			Synchornizer s = sg.getSyner(String.valueOf(i + new Random().nextInt()));
			index[Integer.parseInt(s.getId())]++;
			if (i % 1029 == 0)
				System.out.println(s.getGroup() + ": " + s.getId());
		}
		System.out.println(Arrays.toString(index));
		int max = 0;
		for (int i = 0; i < index.length; i++) {
			if (max < index[i])
				max = index[i];
		}
		System.out.println(max);

	}
}
