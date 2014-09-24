// File: src/main/resources/logback.groovy
import static ch.qos.logback.classic.Level.*
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.status.OnConsoleStatusListener
import ch.qos.logback.core.FileAppender

displayStatusOnConsole()
scan('5 minutes')  // Scan for changes every 5 minutes.
setupAppenders()
setupLoggers()

def displayStatusOnConsole() {
    statusListener OnConsoleStatusListener
}

def setupAppenders() {
    def logfileDate = timestamp('yyyy-MM-dd') // Formatted current date.
    // hostname is a binding variable injected by Logback.
    def filePatternFormat = "%d{HH:mm:ss.SSS} %-5level [${hostname}] %logger - %msg%n"
    appender('logfile', FileAppender) {
        file = "simple.${logfileDate}.log"
        encoder(PatternLayoutEncoder) {
            pattern = filePatternFormat
        }
    }

    appender('systemOut', ConsoleAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = "%-5level %-12logger{12}  %msg%n"
        }
    }
}

def setupLoggers() {
    logger 'com.zanthrash', INFO, ['logfile']
    logger 'net.sf.ehcache', DEBUG, ['logfile']
    root WARN, ['systemOut']
}

def getLogLevel() {
    (isDevelopmentEnv() ? DEBUG : INFO)
}

def isDevelopmentEnv() {
    def env =  System.properties['app.env'] ?: 'DEV'
    env == 'DEV'
}