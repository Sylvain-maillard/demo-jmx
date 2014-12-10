package fr.vsct.quicky.jmx.clientloader;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

import java.util.Stack;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

public class ClientGuiController {

    private final ScheduledExecutorService graphUpdaterThread;
    public Slider threadSlider;
    public LineChart<String, Number> scenarioLineChart;
    public NumberAxis scenarioCountAxis;
    public Button startBtn;
    public Button stopBtn;
    public CheckBox refreshChkBox;

    private ExecutorService executorService;
    private AtomicLong currentSessionCount = new AtomicLong();
    private AtomicLong currentClientCount = new AtomicLong();

    private ScheduledFuture<?> scheduledFuture;
    private Stack<ClientTask> clientTaskStack = new Stack<>();

    public ClientGuiController() {
        executorService = Executors.newFixedThreadPool(20);
        graphUpdaterThread = Executors.newSingleThreadScheduledExecutor();

        startRefresher();
    }

    public void stopRefresher() {
        scheduledFuture.cancel(true);
    }

    public void startRefresher() {
        scheduledFuture = graphUpdaterThread.scheduleAtFixedRate(() -> {
            Platform.runLater(this::updateScenarioLineChart);
        }, 1, 5, TimeUnit.SECONDS);
    }

    @FXML
    protected void initialize() {
        // add a serie.
//        scenarioLineChart.getData().add(new XYChart.Series<String, Number>("Scenario pass", FXCollections.<XYChart.Data<String, Number>>observableArrayList()));
        scenarioLineChart.getData().add(new XYChart.Series<String, Number>("Clients count", FXCollections.<XYChart.Data<String, Number>>observableArrayList()));
        scenarioLineChart.getData().add(new XYChart.Series<String, Number>("Mean Response Time", FXCollections.<XYChart.Data<String, Number>>observableArrayList()));

        scenarioCountAxis.setUpperBound(100);

        // listner for threads:
        threadSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // compute delta:
            int delta = newValue.intValue() - oldValue.intValue();
            if (delta > 0) {
                while (delta > 0) {
                    ClientTask clientTask = addClient();
                    executorService.submit(clientTask);
                    delta--;
                }
            } else if (delta < 0) {
                while (delta < 0) {
                    removeClient();
                    delta++;
                }
            }
        });
    }

    private void removeClient() {
        clientTaskStack.pop().cancel();
        currentClientCount.decrementAndGet();
    }

    private ClientTask addClient() {
        ClientTask clientTask = new ClientTask();
        clientTaskStack.push(clientTask);
        currentClientCount.incrementAndGet();
        return clientTask;
    }

    public void exit() throws InterruptedException {
        stopSessions(null);
        executorService.shutdownNow();
        graphUpdaterThread.shutdownNow();
        Platform.exit();
    }

    public void startSessions(ActionEvent actionEvent) {
        for (int i = 0; i < threadSlider.getValue(); i++) {
            clientTaskStack.push(new ClientTask());
        }
        currentClientCount.set(clientTaskStack.size());
        clientTaskStack.forEach(executorService::submit);

        stopBtn.disableProperty().setValue(false);
        startBtn.disableProperty().setValue(true);
    }

    private void updateScenarioLineChart() {
        try {
//            System.out.println("updating chart." + scenarioLineChart.getData().size() + "," + currentSessionCount.get());
//            XYChart.Series<String, Number> scenarioPassCount = scenarioLineChart.getData().get(0);
            XYChart.Series<String, Number> clientsCount = scenarioLineChart.getData().get(0);
            XYChart.Series<String, Number> responseTime = scenarioLineChart.getData().get(1);
            if (responseTime.getData().size() > 10) {
//                scenarioPassCount.getData().remove(0);
                clientsCount.getData().remove(0);
                responseTime.getData().remove(0);
            }

//            scenarioPassCount.getData().add(new LineChart.Data<>(now().format(ofPattern("HH:mm:ss")), currentSessionCount.get()));
            clientsCount.getData().add(new LineChart.Data<>(now().format(ofPattern("HH:mm:ss")), currentClientCount.get()));
            responseTime.getData().add(new LineChart.Data<>(now().format(ofPattern("HH:mm:ss")), TimeUnit.NANOSECONDS.toMillis((long) timer.getSnapshot().getMean())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopSessions(ActionEvent actionEvent) {
        while (!clientTaskStack.isEmpty()) {
            removeClient();
        }
        stopBtn.disableProperty().setValue(true);
        startBtn.disableProperty().setValue(false);
    }

    public void refreshCheckBox(ActionEvent actionEvent) {
        if (refreshChkBox.isSelected() && scheduledFuture.isCancelled()) {
            startRefresher();
        } else {
            stopRefresher();
        }
    }

    private MetricRegistry metricRegistry = new MetricRegistry();
    Timer timer = metricRegistry.timer("clientloader-response-time");

    public class ClientTask extends Task<Integer> {


        @Override
        protected Integer call() throws Exception {
            while (true) {
                if (isCancelled()) {
                    break;
                }

                Timer.Context time = timer.time();
                Client client1 = new Client();
                client1.runScenario();
                time.stop();
                currentSessionCount.incrementAndGet();
            }
            return null;
        }
    }
}
