package net.poundex.releaseman

class LogEntry
{
	Date dateCreated
	String entry

	static constraints = {
	}

	static mapping = {
		entry(type: "text")
	}
}
