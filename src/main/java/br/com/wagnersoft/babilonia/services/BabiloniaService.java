package br.com.wagnersoft.babilonia.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.wagnersoft.babilonia.dominio.dto.CidadaoConsultDTO;
import br.com.wagnersoft.babilonia.dominio.dto.WSResultDTO;
import br.com.wagnersoft.babilonia.exceptions.BabiloniaException;

/** Interface de um serviço de pesquisa de informações.
 * @author WagnerSoft
 * @since 0.1
 * @version 0.1
 */
public interface BabiloniaService {

  /** Consulta as informações do cidadão.
   * @param consult
   * @return
   * @throws ConectagovException
   */
  WSResultDTO consultService(CidadaoConsultDTO consult) throws BabiloniaException;

  /** Consulta lista de CPF.
   * @param listaCpf
   * @param forca
   * @return Lista WSResultDTO
   * @throws BabiloniaException
   */
  public default List<WSResultDTO> consultService(final String[] listaCpf, final Integer forca) throws BabiloniaException {
    if (listaCpf == null || listaCpf.length == 0) {
      throw new BabiloniaException("Lista de CPF vazia.");
    }
    final List<WSResultDTO> lista = new ArrayList<>(listaCpf.length);
    Arrays.asList(listaCpf).forEach(cpf -> {
      try {
        lista.add(this.consultService(CidadaoConsultDTO.builder().cpf(cpf).build()));
      } catch (BabiloniaException e) {
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
