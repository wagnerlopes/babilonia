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
import br.com.wagnersoft.babilonia.model.Municipio;
import br.com.wagnersoft.babilonia.repository.MunicipioRepository;
import br.com.wagnersoft.babilonia.service.MunicipioService.DistritoDTO;
import br.com.wagnersoft.babilonia.service.MunicipioService.MunicipioDTO;

@ExtendWith(MockitoExtension.class)
class MunicipioServiceTest {

  @Mock
  private MunicipioRepository rep;

  @InjectMocks
  private MunicipioService service;

  private Municipio mun;

  @BeforeEach
  void setup() {
    mun = new Municipio();
    mun.setCodigo(1);
    mun.setDescricao("Município de Teste");
    mun.setDistritos(Collections.emptyList());
  }

  @Test
  void consultById_deveRetornarObjetoQuandoExiste() {
    // Arrange
    Distrito d = new Distrito();
    d.setId(1001);
    d.setDescricao("Distrito Exemplo");

    mun.setDistritos(List.of(d));

    when(rep.findByIdWithDistritos(1)).thenReturn(Optional.of(mun));

    // Act
    MunicipioDTO result = service.consultById(1);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(1);
    assertThat(result.descricao()).isEqualTo("Município de Teste");

    // Valida o mapeamento do List<DistritoDTO>
    assertThat(result.distritos())
    .hasSize(1)
    .extracting(DistritoDTO::id, DistritoDTO::descricao)
    .containsExactly(tuple(1001, "Distrito Exemplo"));

    verify(rep).findByIdWithDistritos(1);
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
    when(rep.findByIdWithDistritos(99)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoDataFoundException.class, () -> service.consultById(99));
    verify(rep).findByIdWithDistritos(99);
  }

}
