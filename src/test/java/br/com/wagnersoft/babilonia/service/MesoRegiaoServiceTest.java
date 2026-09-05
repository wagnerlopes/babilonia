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
import br.com.wagnersoft.babilonia.model.MesoRegiao;
import br.com.wagnersoft.babilonia.model.MicroRegiao;
import br.com.wagnersoft.babilonia.repository.MesoRegiaoRepository;
import br.com.wagnersoft.babilonia.service.MesoRegiaoService.MesoRegiaoDTO;
import br.com.wagnersoft.babilonia.service.MesoRegiaoService.MicroRegiaoDTO;

@ExtendWith(MockitoExtension.class)
class MesoRegiaoServiceTest {

  @Mock
  private MesoRegiaoRepository rep;

  @InjectMocks
  private MesoRegiaoService service;

  private MesoRegiao microrregiao;

  @BeforeEach
  void setup() {
    microrregiao = new MesoRegiao();
    microrregiao.setId(1);
    microrregiao.setDescricao("Mesorregião de Teste");
    microrregiao.setMicroregioes(Collections.emptyList());
  }

  @Test
  void consultById_deveRetornarObjetoQuandoExiste() {
    // Arrange
    MicroRegiao municipio = new MicroRegiao();
    municipio.setId(1001);
    municipio.setDescricao("Microrregião Exemplo");

    microrregiao.setMicroregioes(List.of(municipio));

    when(rep.findByIdWithMicroregioes(1)).thenReturn(Optional.of(microrregiao));

    // Act
    MesoRegiaoDTO result = service.consultById(1);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(1);
    assertThat(result.descricao()).isEqualTo("Mesorregião de Teste");

    // Valida o mapeamento do List<MicroRegiaoDTO>
    assertThat(result.microrregiao())
    .hasSize(1)
    .extracting(MicroRegiaoDTO::id, MicroRegiaoDTO::descricao)
    .containsExactly(tuple(1001, "Microrregião Exemplo"));

    verify(rep).findByIdWithMicroregioes(1);
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
    when(rep.findByIdWithMicroregioes(99)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoDataFoundException.class, () -> service.consultById(99));
    verify(rep).findByIdWithMicroregioes(99);
  }

}
