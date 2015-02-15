# LogZ
A wrapper library for DayZ Standalone log files.

LogZ turns the hard-to-read DayZ Standalone log files (.ADM) into an easily accessible format.

For the time being the log files are not too rich and only contain the following events:
* Player connected
* Player disconnected
* Player killed
* Chat

LogZ is very simple to use, simply feed the log file to it in your preferable way. If no errors occur you will receive a useful
DayZLog object which wraps the log file into useful functions. Then simply call getEventsCopy() or getPlayerCopy() to receive a list
of events and/or players.

Check out the example to see how it's done.
