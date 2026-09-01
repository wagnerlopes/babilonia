package br.com.wagnersoft.babilonia.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
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
import br.com.wagnersoft.babilonia.model.Distrito;
import br.com.wagnersoft.babilonia.model.Localidade;
import br.com.wagnersoft.babilonia.repository.DistritoRepository;
import br.com.wagnersoft.babilonia.service.DistritoService.DistritoDTO;
import br.com.wagnersoft.babilonia.service.DistritoService.LocalidadeDTO;

@ExtendWith(MockitoExtension.class)
class DistritoServiceTest {

  @Mock
  private DistritoRepository rep;

  @InjectMocks
  private DistritoService service;

  private Distrito d;

  @BeforeEach
  void setup() {
    d = new Distrito();
    d.setId(1);
    d.setDescricao("Distrito de Teste");
    d.setLocalidades(Collections.emptyList());
  }

  @Test
  void consultById_deveRetornarObjetoQuandoExiste() {
    // Arrange
    Localidade loc = new Localidade();
    loc.setId(1001);
    loc.setDescricao("Localidade Exemplo");

    d.setLocalidades(List.of(loc));

    when(rep.findByIdWithLocalidades(1)).thenReturn(Optional.of(d));

    // Act
    DistritoDTO result = service.consultById(1);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(1);
    assertThat(result.descricao()).isEqualTo("Distrito de Teste");

    // Valida o mapeamento do List<LocalidadeDTO>
    assertThat(result.localidades())
    .hasSize(1)
    .extracting(LocalidadeDTO::id, LocalidadeDTO::descricao)
    .containsExactly(tuple(1001, "Localidade Exemplo"));

    verify(rep).findByIdWithLocalidades(1);
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
    when(rep.findByIdWithLocalidades(99)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoDataFoundException.class, () -> service.consultById(99));
    verify(rep).findByIdWithLocalidades(99);
  }

  @Test
  void consultByDescricao_deveRetornarListaVaziaQuandoDescricaoNulaOuEmBranco() {
    // Act
    List<DistritoDTO> resultadoNulo = service.consultByDescricao(null);
    List<DistritoDTO> resultadoVazio = service.consultByDescricao("   ");

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
    Localidade loc = new Localidade();
    loc.setId(2001);
    loc.setDescricao("Bairro Centro");

    d.setLocalidades(List.of(loc));

    when(rep.findByDescricao("Distrito de Teste")).thenReturn(List.of(d));

    // Act
    List<DistritoDTO> resultado = service.consultByDescricao("Distrito de Teste");

    // Assert
    assertThat(resultado).hasSize(1);

    DistritoDTO dto = resultado.get(0);
    assertThat(dto.id()).isEqualTo(1);
    assertThat(dto.descricao()).isEqualTo("Distrito de Teste");

    assertThat(dto.localidades())
    .hasSize(1)
    .extracting(LocalidadeDTO::id, LocalidadeDTO::descricao)
    .containsExactly(tuple(2001, "Bairro Centro"));

    verify(rep).findByDescricao("Distrito de Teste");
  }  

}
