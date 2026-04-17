package br.mil.eb.wagnersoft.babilonia.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.mil.eb.wagnersoft.babilonia.dominio.dto.CidadaoConsultDTO;
import br.mil.eb.wagnersoft.babilonia.dominio.dto.WSResultDTO;
import br.mil.eb.wagnersoft.babilonia.ConectagovException;

/** Interface de um serviço de pesquisa de informações em um subsistema.
 * @author WagnerSoft
 * @since 0.1
 * @version 0.1
 */
public interface ConectagovService {

  /** Consulta as informações do cidadão.
   * @param consult
   * @return
   * @throws ConectagovException
   */
  WSResultDTO consultService(CidadaoConsultDTO consult) throws ConectagovException;

  /** Consulta lista de CPF.
   * @param listaCpf
   * @param forca
   * @return Lista WSResultDTO
   * @throws ConectagovException
   */
  public default List<WSResultDTO> consultService(final String[] listaCpf, final Integer forca) throws ConectagovException {
    if (listaCpf == null || listaCpf.length == 0) {
      throw new ConectagovException("Lista de CPF vazia.");
    }
    final List<WSResultDTO> lista = new ArrayList<>(listaCpf.length);
    Arrays.asList(listaCpf).forEach(cpf -> {
      try {
        lista.add(this.consultService(CidadaoConsultDTO.builder().cpf(cpf).forca(forca).build()));
      } catch (ConectagovException e) {
        lista.add(
          WSResultDTO.builder()
            .cpf(cpf)
            .consultaData(LocalDate.now())
            .situacaoDescricao(e.getMessage())
            .build());
      }
    });
    return lista;
  }
  
}
