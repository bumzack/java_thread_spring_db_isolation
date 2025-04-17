package at.bumzack.bff.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private final Logger LOG = LogManager.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        // code am weg zum controller
        // request analysieren

        final var requestURI = request.getRequestURI(); // "/dummy"
        final var method = request.getMethod();
        final var feReqId = request.getHeader("fe-req-id");
        final var stopWatch = new StopWatch();
        final var threadId = Thread.currentThread().threadId();
        stopWatch.start();

        LOG.info("start {}:{}, feReqId={}, threadId {}", method, requestURI, feReqId, threadId);

        filterChain.doFilter(request, response);

        // code am weg vom filter zurück : response manipulieren?

        stopWatch.stop();
        final var duration = stopWatch.getTotalTimeMillis();
        LOG.info("end {}:{}, feReqId={}, threadId {} took {}ms", requestURI, method, feReqId, threadId, duration);

    }
}
