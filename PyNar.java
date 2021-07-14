package org.opennars.main;

import org.opennars.entity.Sentence;
import org.opennars.io.events.EventHandler;
import org.opennars.entity.Task;
import org.opennars.io.events.OutputHandler;
import org.opennars.io.events.TextOutputHandler;
import org.opennars.io.events.Events.Answer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;

public class PyNar {
    private final Nar nar;
    private Queue<Object> queue_out = new LinkedList<Object>();
    private EventHandler out;

    protected boolean showErrors = true;
    protected boolean showStamp = true;
    protected boolean showQuestions = true;
    protected boolean showStatements = true;
    protected boolean showExecutions = true;

    private boolean is_debug = false;

    public static final Class[] outputEvents = OutputHandler.DefaultOutputEvents;

    public PyNar() throws Exception{
        System.out.println("PyNar: init...");

        this.nar = new Nar();

        out = new EventHandler(nar, true, outputEvents) {
            @Override public void event(final Class event, final Object[] arguments) {
                PyNar.this.output(event, arguments.length > 1 ? arguments : arguments[0]);
            }
        };
    }

    private void output(final Class c, Object o) {
        Object[] pair = {c, o};
        if (c == OutputHandler.EXE.class) {
            queue_out.offer(pair);
        }
        if (c == Answer.class) {
            queue_out.offer(pair);
        }


        if ((c == OutputHandler.ERR.class) && (!showErrors)) {
            return;
        }
        if ((c == OutputHandler.EXE.class) && !showExecutions) {
            return;
        }

        if (o instanceof Task) {

            Sentence s = ((Task) o).sentence;
            if (s!=null) {
                if (s.isQuestion() && !showQuestions) {
                    return;
                }
                if (s.isJudgment() && !showStatements) {
                    return;
                }
                if (s.isGoal()&& !showExecutions) {
                    return;
                }
            }


        }

        print(c, o);

    }

    public String addInput(String input) {
        try{
            nar.addInput(input);
        }
        catch(Exception ex) {
            if(Debug.DETAILED) {
                throw new IllegalStateException("error parsing:" + input, ex);
            }

            nar.memory.emit(OutputHandler.ECHO.class, "Parsing failed!");

            return input;
        }
        nar.cycles(1);
        return "";
    }

    public Queue<Object> get_queue() {
        return this.queue_out;
    }

    public Map<String, Object> cycles(int max_cycles) {
        queue_out.clear();

        int cycles_left = 0;
        for(int i = 0; i<max_cycles; i++) {
            nar.cycles(1);
//            nar.cycle();
            if (queue_out.size() > 0) {
                cycles_left = max_cycles - i;
                break;
            }
        }


        Map<String, Object> ret_map = new HashMap<String, Object>();
        ret_map.put("cycles_left", cycles_left);
        ret_map.put("queue_out", queue_out);
        return ret_map;
    }

    private String filter = "";
    protected PrintWriter logFile = null;
    private void print(Class c, Object o) {

        if(!filter.equals("")) { //ok its filtered we can afford the ToStrings for this case as we also want the logfile to be affected
            if(!o.toString().contains(filter)) {
                return;
            }
        }
        CharSequence s = TextOutputHandler.getOutputString(c, o, true, showStamp, nar);
        if (logFile != null) {
            logFile.append(s).append('\n');
        }
        if (is_debug) {
            System.out.println(s);
        }
    }

    private String logFilePath = null;
    public void set_log_path(String path) {
        logFilePath = path;
    }

    public static final class LOG {
    }

    public boolean start_log(boolean append) {
        if (logFilePath == null) {
            return false;
        }

        try {
            boolean autoflush = true;
            logFile = new PrintWriter(new FileWriter(logFilePath, append), autoflush);
            output(LOG.class, "Stream opened: " + logFilePath);
            return true;
        } catch (IOException ex) {
            output(OutputHandler.ERR.class, "Log file save: I/O error: " + ex.getMessage());
        }

        return false;
    }

    public void stop_log() {
        if (logFile != null) {
            output(LOG.class, "Stream saved: " + logFilePath);
            logFile.close();
            logFile = null;
        }
    }

    public void debug(boolean d) {
        is_debug = d;
    }

    public void debug() {
        debug(true);
    }

    public static void main(String args[]) throws Exception {
        System.out.println("PyNar: start main...");
        PyNar pynar = new PyNar();
        pynar.addInput("<bird-->animal>.");
        pynar.addInput("<robin-->bird>.");
        pynar.addInput("<robin-->animal>?");
        pynar.addInput("5");
        pynar.addInput("<(*,{SELF})-->^left>! :|:");
        pynar.addInput("30");
        Queue<Object> queue = pynar.get_queue();
    }







}
