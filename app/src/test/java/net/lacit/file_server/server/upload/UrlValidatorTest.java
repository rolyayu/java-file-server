package net.lacit.file_server.server.upload;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import net.lacit.file_server.server.exceptions.BadRequestException;
import net.lacit.file_server.server.exceptions.UnsupportedMediaTypeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlValidatorTest {
    private UrlValidator urlValidator;

    @Mock
    private HttpExchange mockExchange;

    @Mock
    private URI mockURI;

    @BeforeEach
    void beforeEach() {
        urlValidator = new UrlValidator();
    }

    @Test
    void UrlValidator_ValidateUrl_ThrowsWhenPathParamsNotGiven() {
        given(mockExchange.getRequestURI()).willReturn(mockURI);
        given(mockURI.getPath()).willReturn("");

        Assertions.assertThrows(BadRequestException.class, () -> {
            urlValidator.validateUrl(mockExchange);
        });
        verify(mockExchange, times(1)).getRequestURI();
        verify(mockURI, times(1)).getPath();
    }

    @Test
    void UrlValidator_ValidateUrl_ThrowsWhenGivenWrongFileExtension() {
        given(mockExchange.getRequestURI()).willReturn(mockURI);
        given(mockURI.getPath()).willReturn("file.ext");

        Assertions.assertThrows(UnsupportedMediaTypeException.class, () -> {
            urlValidator.validateUrl(mockExchange);
        });
        verify(mockExchange, times(1)).getRequestURI();
        verify(mockURI, times(1)).getPath();
    }

    @Test
    void UrlValidator_ValidateUrl_ThrowsWhenGivenMultipleFiles() {
        given(mockExchange.getRequestURI()).willReturn(mockURI);
        given(mockExchange.getRequestMethod()).willReturn("GET");
        given(mockURI.getPath()).willReturn("file.ext/file.pdf");

        Assertions.assertThrows(BadRequestException.class, () -> {
            urlValidator.validateUrl(mockExchange);
        });
        verify(mockExchange, times(1)).getRequestURI();
        verify(mockURI, times(1)).getPath();
    }

    @Test
    void UrlValidator_ValidateUrl_ThrowsContentTypeEmpty() {
        Headers mockHeaders = mock(Headers.class);
        given(mockExchange.getRequestURI()).willReturn(mockURI);
        given(mockExchange.getRequestMethod()).willReturn("POST");
        given(mockExchange.getRequestHeaders()).willReturn(mockHeaders);
        given(mockHeaders.get(anyString())).willReturn(List.of());
        given(mockURI.getPath()).willReturn("file.ext/file.pdf");

        Assertions.assertThrows(UnsupportedMediaTypeException.class, () -> {
            urlValidator.validateUrl(mockExchange);
        });
        verify(mockExchange, times(1)).getRequestURI();
        verify(mockURI, times(1)).getPath();
        verify(mockExchange, times(1)).getRequestHeaders();
        verify(mockHeaders, times(1)).get(anyString());
    }

    @Test
    void UrlValidator_ValidateUrl_ThrowsWhenMismatchBetweenContentTypeAndExtension() {
        Headers mockHeaders = mock(Headers.class);
        given(mockExchange.getRequestURI()).willReturn(mockURI);
        given(mockExchange.getRequestMethod()).willReturn("POST");
        given(mockExchange.getRequestHeaders()).willReturn(mockHeaders);
        given(mockHeaders.get(anyString())).willReturn(List.of("text/plain"));
        given(mockURI.getPath()).willReturn("file.ext/file.pdf");

        Assertions.assertThrows(BadRequestException.class, () -> {
            urlValidator.validateUrl(mockExchange);
        });
        verify(mockExchange, times(1)).getRequestURI();
        verify(mockURI, times(1)).getPath();
        verify(mockExchange, times(1)).getRequestHeaders();
        verify(mockHeaders, times(1)).get(anyString());
    }

    @Test
    void UrlValidator_ValidateUrl_DoNothingOnSuccess() {
        Headers mockHeaders = mock(Headers.class);
        given(mockExchange.getRequestURI()).willReturn(mockURI);
        given(mockExchange.getRequestMethod()).willReturn("POST");
        given(mockExchange.getRequestHeaders()).willReturn(mockHeaders);
        given(mockHeaders.get(anyString())).willReturn(List.of("application/pdf"));
        given(mockURI.getPath()).willReturn("ext/file.pdf");

        urlValidator.validateUrl(mockExchange);

        verify(mockExchange, times(1)).getRequestURI();
        verify(mockURI, times(1)).getPath();
        verify(mockExchange, times(1)).getRequestHeaders();
        verify(mockHeaders, times(1)).get(anyString());
    }
}
