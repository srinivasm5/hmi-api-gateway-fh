package uk.gov.hmcts.futurehearings.hmi.acceptance.common.header.dto.factory;

import uk.gov.hmcts.futurehearings.hmi.acceptance.common.header.dto.BusinessHeaderDTO;
import uk.gov.hmcts.futurehearings.hmi.acceptance.common.header.dto.SystemHeaderDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class PayloadHeaderDTOFactory {

    public static final SystemHeaderDTO buildStandardSytemHeaderPart(final String contentType,
                                                                     final String accept,
                                                                     final String authorization,
                                                                     final String contentEncoding,
                                                                     final String subscriptionKey,
                                                                     final String cacheControl) {
        return SystemHeaderDTO.builder()
                .contentType(contentType)
                .accept(accept)
                .authorization(authorization)
                .contentEncoding(contentEncoding)
                .cacheControl(cacheControl)
                .subscriptionKey(subscriptionKey).build();
    }

    public static final BusinessHeaderDTO buildStandardBuinessHeaderPart(final String requestCreatedAt,
                                                                         final String requestProcessedAt,
                                                                         final String sourceSystem,
                                                                         final String destinationSystem,
                                                                         final String requestType) {
        return BusinessHeaderDTO.builder()
                .requestCreatedAt(requestCreatedAt)
                .requestProcessedAt(requestProcessedAt)
                .sourceSystem(sourceSystem)
                .destinationSystem(destinationSystem)
                .requestType(requestType).build();
    }

    public static final Map<String,String> convertToMapWithMandatoryHeaders(final SystemHeaderDTO systemHeaderDTO,
                                                                            final BusinessHeaderDTO businessHeaderDTO) {
        final Map<String, String>  headerMap = new HashMap<>();
        headerMap.put("Ocp-Apim-Subscription-Key",systemHeaderDTO.subscriptionKey());
        headerMap.put("Content-Type",systemHeaderDTO.contentType());
        headerMap.put("Accept",systemHeaderDTO.accept());
        headerMap.put("Source-System",businessHeaderDTO.sourceSystem());
        headerMap.put("Destination-System",businessHeaderDTO.destinationSystem());
        headerMap.put("Request-Created-At",businessHeaderDTO.requestCreatedAt());
        headerMap.put("Request-Processed-At",businessHeaderDTO.requestProcessedAt());
        headerMap.put("Request-Type",businessHeaderDTO.requestType());
        return headerMap;
    }

    public static final Map<String,String> convertToMapWithAllHeaders(final SystemHeaderDTO systemHeaderDTO,
                                                                            final BusinessHeaderDTO businessHeaderDTO) {
        final Map<String,String> headerMap = convertToMapWithMandatoryHeaders(systemHeaderDTO,businessHeaderDTO);
        headerMap.put("Cache-Control",systemHeaderDTO.cacheControl());
        headerMap.put("Content-Encoding",systemHeaderDTO.contentEncoding());
        return headerMap;
    }

    public static final Map<String,String> convertToMapAfterHeadersRemoved (final SystemHeaderDTO systemHeaderDTO,
                                                         final BusinessHeaderDTO businessHeaderDTO,
                                                         final List<String> headersToRemove) {

        final Map<String, String>  headerMap = convertToMapWithMandatoryHeaders(systemHeaderDTO,businessHeaderDTO);
        if (Objects.nonNull(headersToRemove)) {
            headersToRemove.stream().forEach((o)-> {
                headerMap.remove(o.trim());
            });
        }
        return headerMap;
    }

    public static final Map<String,String> convertToMapAfterTruncatingHeaderKey (final SystemHeaderDTO systemHeaderDTO,
                                                                          final BusinessHeaderDTO businessHeaderDTO,
                                                                          final List<String> headersToTruncate) {

        final Map<String, String>  headerMap = convertToMapWithMandatoryHeaders(systemHeaderDTO,businessHeaderDTO);
        if (Objects.nonNull(headersToTruncate)) {
            headersToTruncate.stream().forEach((o)-> {
                if (headerMap.containsKey(o)) {
                    String newKey = o.substring(0, o.length()-1);
                    String newValue = headerMap.get(o);
                    headerMap.remove(o);
                    headerMap.put(newKey, newValue);
                };
            });
        }
        return headerMap;
    }
}
