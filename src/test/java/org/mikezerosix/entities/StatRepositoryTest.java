package org.mikezerosix.entities;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mikezerosix.AppTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class StatRepositoryTest {


    private StatRepository statRepository;

    private final AnnotationConfigApplicationContext context;

    public StatRepositoryTest() {
        context = new AnnotationConfigApplicationContext(AppTest.class);
        statRepository = (StatRepository) context.getBean("statRepository");
    }

    @Test
    public void testIsWired() throws Exception {
        assertThat(statRepository, CoreMatchers.notNullValue());
    }

    @Test
    public void testGetLastStat() throws Exception {
        final List<Stat> lastStat = statRepository.getLastStat(new PageRequest(0, 1));
    }

    @Test
    public void testGetMaxStat() throws Exception {
        statRepository.getMaxStat();
    }
}