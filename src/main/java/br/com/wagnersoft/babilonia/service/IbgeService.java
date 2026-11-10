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
 * IbgeService.
 * 
 * @since 1.0.5
 * @version 1.0.5
 * @author Wagner Lopes
 */
@Service
public class IbgeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(IbgeService.class);

  private final Map<String, Long> municipios = new HashMap<>();

  private final HttpClient httpClient;

  private final ObjectMapper mapper;

  public IbgeService() {
    this.httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
    this.mapper = new ObjectMapper();
    loadMunicipios();
  }

  private void loadMunicipios() {

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
  
  // Teste 
  public static void main(String[] args) {
    IbgeService svc = new IbgeService();
    LOGGER.info("{}", svc.obterId("São Paulo", "SP"));
  }

}
