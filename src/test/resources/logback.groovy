scan("15 seconds")

def Hostname = "${hostname}"

appender("app-appender", RollingFileAppender) {
	encoder(PatternLayoutEncoder) {
		pattern = "%d{yyyyMMddHHmmss.SSS};%C{0};${Hostname};%thread;%mdc{req_id};%.-1level;%msg;%n"
	}
	rollingPolicy(TimeBasedRollingPolicy) {
		fileNamePattern = 'logs/%d{yyyyMMdd}_springbootdemo_test.log.gz'
			maxHistory = 30
	}
}

logger("org.springframework", INFO, ["app-appender"], false)
logger("org.maxwell.springboot",DEBUG, ["app-appender"], false)

root(DEBUG, ["app-appender"])