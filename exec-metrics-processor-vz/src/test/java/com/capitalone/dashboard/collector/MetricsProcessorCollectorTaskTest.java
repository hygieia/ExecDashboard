package com.capitalone.dashboard.collector;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.exec.model.vz.Collector;
import com.capitalone.dashboard.exec.model.vz.CollectorType;
import com.capitalone.dashboard.exec.repository.vz.BaseCollectorRepository;

@RunWith(MockitoJUnitRunner.class)
public class MetricsProcessorCollectorTaskTest {

	@InjectMocks
	private MetricsProcessorCollectorTask metricsProcessorCollectorTask;
	@Mock
	private MetricsProcessorCollectorClient metricsProcessorCollectorClient;
	@Mock
	private BaseCollectorRepository<Collector> collectorRepository;
	@Mock
	private MetricsProcessorSettings metricsProcessorSettings;

	@Test
	public void testGetCron() {
		String result = metricsProcessorCollectorTask.getCron();
		assertEquals(null, result);
	}

	@Test
	public void testMetricsProcessorCollectorTask() {
		Collector collector = new Collector();
		collector.setName("MetricsProcessor");
		collector.setCollectorType(CollectorType.MetricsProcessor);
		collector.setOnline(true);
		collector.setEnabled(true);
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		list.add(9);
		list.add(10);
		list.add(11);
		list.add(12);
		list.add(13);
		list.add(14);
		list.add(15);
		list.add(16);
		Mockito.when(metricsProcessorSettings.getMetrics()).thenReturn(list);
		metricsProcessorCollectorTask.collect(collector);
	}

	@Test
	public void testGetCollector() {
		metricsProcessorCollectorTask.getCollector();
	}

	@Test
	public void testGetCollectorRepository() {
		metricsProcessorCollectorTask.getCollectorRepository();
	}

	@Test
	public void testCollectCollector() {
		Collector collector = new Collector();
		metricsProcessorCollectorTask.collect(collector);
	}

}
