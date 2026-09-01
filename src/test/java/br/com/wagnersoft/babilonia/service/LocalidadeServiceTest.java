package br.com.wagnersoft.babilonia.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.Categoria;
import br.com.wagnersoft.babilonia.model.Coordenada;
import br.com.wagnersoft.babilonia.model.Distrito;
import br.com.wagnersoft.babilonia.model.Localidade;
import br.com.wagnersoft.babilonia.model.MesoRegiao;
import br.com.wagnersoft.babilonia.model.MicroRegiao;
import br.com.wagnersoft.babilonia.model.Municipio;
import br.com.wagnersoft.babilonia.model.TipoCategoria;
import br.com.wagnersoft.babilonia.model.Uf;
import br.com.wagnersoft.babilonia.model.dto.LocalidadeDTO;
import br.com.wagnersoft.babilonia.repository.LocalidadeRepository;

@ExtendWith(MockitoExtension.class)
class LocalidadeServiceTest {

  @Mock
  private LocalidadeRepository rep;

  @InjectMocks
  private LocalidadeService service;

  private Localidade loc;

  @BeforeEach
  void setup() {
    // Configuração do encadeamento da árvore de entidades para a Localidade
    Uf uf = new Uf();
    uf.setSigla("SP");

    MesoRegiao mesoregiao = new MesoRegiao();
    mesoregiao.setDescricao("Campinas");
    mesoregiao.setUf(uf);

    MicroRegiao microregiao = new MicroRegiao();
    microregiao.setDescricao("Jundiaí");
    microregiao.setMesoregiao(mesoregiao);

    Municipio municipio = new Municipio();
    municipio.setDescricao("Itupeva");
    municipio.setMicroregiao(microregiao);

    Distrito distrito = new Distrito();
    distrito.setDescricao("Distrito Sede");
    distrito.setMunicipio(municipio);

    Categoria categoria = new Categoria();
    categoria.setDescricao(TipoCategoria.CIDADE);

    Coordenada coordenada = new Coordenada();
    coordenada.setLatitude(-23.1534);
    coordenada.setLongitude(-47.0578);
    coordenada.setAltitude(675.0);

    loc = new Localidade();
    loc.setId(1);
    loc.setDescricao("Centro");
    loc.setTipo("Cidade");
    loc.setNivel(1); // Supondo tipo primitivo ou objeto cujo toString() seja chamado
    loc.setCategoria(categoria);
    loc.setBairro("Bairro Central");
    loc.setSubdistrito("Subdistrito 1");
    loc.setDistrito(distrito);
    loc.setCoordenada(coordenada);
  }

  // --- Testes para consultById ---

  @Test
  void consultById_deveRetornarDTOQuandoExiste() {
    // Arrange
    when(rep.findById(1)).thenReturn(Optional.of(loc));

    // Act
    LocalidadeDTO result = service.consultById(1);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1);
    assertThat(result.getDescricao()).isEqualTo("Centro");
    assertThat(result.getTipo()).isEqualTo("Cidade");
    assertThat(result.getBairro()).isEqualTo("Bairro Central");
    assertThat(result.getDistrito()).isEqualTo("Distrito Sede");
    assertThat(result.getMunicipio()).isEqualTo("Itupeva");
    assertThat(result.getMicroregiao()).isEqualTo("Jundiaí");
    assertThat(result.getMesoregiao()).isEqualTo("Campinas");
    assertThat(result.getUf()).isEqualTo("SP");
    assertThat(result.getLatitude()).isEqualTo("-23.1534");
    assertThat(result.getLongitude()).isEqualTo("-47.0578");
    assertThat(result.getAltitude()).isEqualTo("675.0");

    verify(rep).findById(1);
  }

  @Test
  void consultById_deveLancarExcecaoQuandoIdNulo() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> service.consultById(null));
    verifyNoInteractions(rep);
  }

  @Test
  void consultById_deveLancarExcecaoQuandoNaoEncontrado() {
    // Arrange
    when(rep.findById(99)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoDataFoundException.class, () -> service.consultById(99));
    verify(rep).findById(99);
  }

  // --- Testes para consultByDescricao ---

  @Test
  void consultByDescricao_deveRetornarListaVaziaQuandoDescricaoNulaOuEmBranco() {
    // Act
    List<LocalidadeDTO> resultadoNulo = service.consultByDescricao(null);
    List<LocalidadeDTO> resultadoVazio = service.consultByDescricao("   ");

    // Assert
    assertThat(resultadoNulo).isEmpty();
    assertThat(resultadoVazio).isEmpty();
    verifyNoInteractions(rep);
  }

  @Test
  void consultByDescricao_deveLancarExcecaoQuandoNaoEncontrarRegistros() {
    // Arrange
    when(rep.findByDescricao("Inexistente")).thenReturn(Collections.emptyList());

    // Act & Assert
    assertThrows(NoDataFoundException.class, () -> service.consultByDescricao("Inexistente"));
    verify(rep).findByDescricao("Inexistente");
  }

  @Test
  void consultByDescricao_deveRetornarListaQuandoEncontrarRegistros() {
    // Arrange
    when(rep.findByDescricao("Centro")).thenReturn(List.of(loc));

    // Act
    List<LocalidadeDTO> resultado = service.consultByDescricao("Centro");

    // Assert
    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getId()).isEqualTo(1);
    assertThat(resultado.get(0).getDescricao()).isEqualTo("Centro");
    assertThat(resultado.get(0).getMunicipio()).isEqualTo("Itupeva");

    verify(rep).findByDescricao("Centro");
  }

  // --- Testes para distancia ---

  @Test
  void distancia_deveCalcularDistanciaQuandoOrigemEDestinoExistem() {
    // Arrange
    Coordenada coordDestino = new Coordenada();
    coordDestino.setAltitude(0.0);
    coordDestino.setLatitude(-23.5505);
    coordDestino.setLongitude(-46.6333);

    Localidade destino = new Localidade();
    destino.setId(2);
    destino.setCoordenada(coordDestino);

    when(rep.findById(1)).thenReturn(Optional.of(loc));
    when(rep.findById(2)).thenReturn(Optional.of(destino));

    // Act
    double distanciaCalculada = service.distancia(1, 2);

    // Assert
    assertThat(distanciaCalculada).isGreaterThanOrEqualTo(0.0);
    verify(rep).findById(1);
    verify(rep).findById(2);
  }

  @Test
  void distancia_deveLancarExcecaoQuandoOrigemNaoEncontrada() {
    // Arrange
    when(rep.findById(1)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoDataFoundException.class, () -> service.distancia(1, 2));
    verify(rep).findById(1);
  }

  @Test
  void distancia_deveLancarExcecaoQuandoDestinoNaoEncontrado() {
    // Arrange
    when(rep.findById(1)).thenReturn(Optional.of(loc));
    when(rep.findById(2)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoDataFoundException.class, () -> service.distancia(1, 2));
    verify(rep).findById(1);
    verify(rep).findById(2);
  }

}
