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
import br.com.wagnersoft.babilonia.model.MicroRegiao;
import br.com.wagnersoft.babilonia.model.Municipio;
import br.com.wagnersoft.babilonia.repository.MicroRegiaoRepository;
import br.com.wagnersoft.babilonia.service.MicroRegiaoService.MicroRegiaoDTO;
import br.com.wagnersoft.babilonia.service.MicroRegiaoService.MunicipioDTO;

@ExtendWith(MockitoExtension.class)
class MicroRegiaoServiceTest {

  @Mock
  private MicroRegiaoRepository rep;

  @InjectMocks
  private MicroRegiaoService service;

  private MicroRegiao microrregiao;

  @BeforeEach
  void setup() {
    microrregiao = new MicroRegiao();
    microrregiao.setId(1);
    microrregiao.setDescricao("Microrregião de Teste");
    microrregiao.setMunicipios(Collections.emptyList());
  }

  @Test
  void consultById_deveRetornarObjetoQuandoExiste() {
    // Arrange
    Municipio municipio = new Municipio();
    municipio.setCodigo(1001);
    municipio.setDescricao("Município Exemplo");

    microrregiao.setMunicipios(List.of(municipio));

    when(rep.findByIdWithMunicipios(1)).thenReturn(Optional.of(microrregiao));

    // Act
    MicroRegiaoDTO result = service.consultById(1);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(1);
    assertThat(result.descricao()).isEqualTo("Microrregião de Teste");

    // Valida o mapeamento do List<MunicipioDTO>
    assertThat(result.municipios())
    .hasSize(1)
    .extracting(MunicipioDTO::id, MunicipioDTO::descricao)
    .containsExactly(tuple(1001, "Município Exemplo"));

    verify(rep).findByIdWithMunicipios(1);
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
    when(rep.findByIdWithMunicipios(99)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoDataFoundException.class, () -> service.consultById(99));
    verify(rep).findByIdWithMunicipios(99);
  }

}
