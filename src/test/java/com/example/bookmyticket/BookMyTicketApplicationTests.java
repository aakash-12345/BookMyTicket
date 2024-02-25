package com.example.bookmyticket;

import lombok.RequiredArgsConstructor;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
@Ignore
public class BookMyTicketApplicationTests {

    private final BookMyTicketApplication bookMyTicketApplication;

    protected final TestRestTemplate testRestTemplate;


}
