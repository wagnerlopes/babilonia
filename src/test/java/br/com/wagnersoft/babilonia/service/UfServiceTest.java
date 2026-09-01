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
import br.com.wagnersoft.babilonia.model.Uf;
import br.com.wagnersoft.babilonia.repository.UfRepository;
import br.com.wagnersoft.babilonia.service.UfService.MesoRegiaoDTO;
import br.com.wagnersoft.babilonia.service.UfService.UfDTO;

@ExtendWith(MockitoExtension.class)
class UfServiceTest {

  @Mock
  private UfRepository rep;

  @InjectMocks
  private UfService service;

  private Uf b1;

  @BeforeEach
  void setup() {
    b1 = new Uf();
    b1.setId(1);
    b1.setSigla("AC");
    b1.setDescricao("Acre");
    b1.setMesoregioes(Collections.emptyList());
  }

  @Test
  void consultBySigla_deveRetornarObjetoQuandoExiste() {
    // Arrange
    MesoRegiao meso = new MesoRegiao();
    meso.setId(10);
    meso.setDescricao("Vale do Juruá");

    b1.setMesoregioes(List.of(meso));

    when(rep.findBySiglaWithMesoRegiao("AC")).thenReturn(Optional.of(b1));

    // Act
    UfDTO result = service.consultBySigla("AC");

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.uf()).isEqualTo("AC");
    assertThat(result.descricao()).isEqualTo("Acre");

    // Validação fluente da lista de mesorregiões
    assertThat(result.mesorregiao())
    .hasSize(1)
    .extracting(MesoRegiaoDTO::id, MesoRegiaoDTO::descricao)
    .containsExactly(tuple(10, "Vale do Juruá"));

    verify(rep).findBySiglaWithMesoRegiao("AC");
  }

  @Test
  void consultBySigla_deveRetornarErrorQuandoNulo() {
    assertThrows(IllegalArgumentException.class, () -> service.consultBySigla(null));
    verifyNoInteractions(rep);
  }

  @Test
  void consultBySigla_deveRetornarErrorQuandoVazio() {
    assertThrows(IllegalArgumentException.class, () -> service.consultBySigla("   "));
    verifyNoInteractions(rep);
  }

  @Test
  void consultBySigla_deveRetornarErrorQuandoNaoExiste() {
    when(rep.findBySiglaWithMesoRegiao("KK")).thenReturn(Optional.empty());

    assertThrows(NoDataFoundException.class, () -> service.consultBySigla("KK"));
    verify(rep).findBySiglaWithMesoRegiao("KK");
  }

}
