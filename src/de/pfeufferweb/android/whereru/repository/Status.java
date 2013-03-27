package de.pfeufferweb.android.whereru.repository;

public enum Status {

	RUNNING(1), SUCCESS(2), NO_LOCATION(3), ABORTED(4), NO_GPS(5), NETWORK(6), NETWORK_NO_GPS(
			7);

	private final int id;

	private Status(int id) {
		this.id = id;
	}

	int getId() {
		return id;
	}

	static Status getForId(int id) {
		for (Status s : values()) {
			if (s.id == id) {
				return s;
			}
		}
		throw new IllegalArgumentException("no status for id " + id);
	}
}
