package de.spiderlinker.io.filter;

import java.io.FileFilter;

public class Filter {
	/**
	 * File will be accepted if its extension is equals passed extension!
	 *
	 * @param extension
	 *          Extension of a file. (always with ".") Example: '.txt', '.jar', '.java' etc.
	 */
	public static FileFilter createFileFilter(final String extension) {
		/*
		 * This FileFilter will only check for the extension of the file
		 */
		return file -> file.getName().endsWith(extension);
	}

	/**
	 * File will be accepted if its extension matches one of the passed extensions!
	 *
	 * @param extensions
	 *          Extensions of a file. (always with ".") Example: '.txt', '.jar', '.java', '.exe' etc.
	 */
	public static FileFilter createFileFilter(final String... extensions) {

		/*
		 * This FileFilter will only check for the extensions of the file
		 */
		return file -> {
			/* signalizes, if the file has the correct extension */
			boolean accept = false;

			/* Get name of file (name + extension) */
			final String name = file.getName();

			/*
			 * Check, if passed extensions are not null / empty!
			 */
			if (extensions != null) {
				/* Go through all passed extensions */
				for (final String extension : extensions) {
					/* Check, if the file ends with the current extension */
					if (name.endsWith(extension)) {
						/*
						 * File extension matches with passed extension
						 * Set 'accept' true and break (file ends with extension)!
						 */
						accept = true;
						break;
					}
				}
			}

			/* returns true, if the file ends with the passed extensions */
			return accept;
		};
	}
}
