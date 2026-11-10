package br.com.wagnersoft.babilonia.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * Serviço responsável por ler da API do IBGE os códigos de municípios.
 * 
 * <p>Os códigos são armazenados em um cache do tipo {@code Map<String, Long>}</code>
 * onde a chave é o nome do município e a UF.</p>
 * 
 * <p>O método {@code obterID(nome, uf)} permite ler o cache e obter o
 * código IBGE do município.</p>
 * 
 * <p>O método {@code isAtivo()} informa se o cache está carregado com os
 * valores esperados.</p>
 * 
 * @since 1.0
 * @version 1.0
 * @author Wagner Lopes
 */
@Service
public class IbgeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(IbgeService.class);

  private final Map<String, Long> municipios = HashMap.newHashMap(5571);

  private final HttpClient httpClient;

  private final ObjectMapper mapper;

  public IbgeService() {
    this.httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
    this.mapper = new ObjectMapper();
    this.initCache();
  }

  private void initCache() {

    try {

      String url = "https://servicodados.ibge.gov.br/api/v1/localidades/municipios?view=nivelado";

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("Accept", "application/json")
          .GET()
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      JsonNode root = mapper.readTree(response.body());

      if (!root.isArray() || root.isEmpty()) {
        throw new RuntimeException("Nenhum dado encontrado");
      }

      for (JsonNode node : root) {
        Long munId = node.get("municipio-id").asLong();
        String munNome = node.get("municipio-nome").asString();
        String munUf = node.get("UF-sigla").asString();
        municipios.put(chave(munNome, munUf), munId);
      }
      LOGGER.info("Total = {}", municipios.size());
    } catch (Exception e) {
      throw new RuntimeException("Erro ao consultar municípios", e);
    }

  }

  private String chave(String nome, String uf) {
    return Normalizer.normalize(nome, Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "")
        .toUpperCase(Locale.ROOT)
        .trim()
        + "|"
        + uf.toUpperCase().trim();
  }

  public Long obterId (String nome, String uf) {
    return municipios.get(chave(nome, uf));
  }

  public boolean isAtivo() {
    return !this.municipios.isEmpty();
  }

}
