// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software distributed under the
// License is distributed on
// * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
// the License for the
// * specific language governing permissions and limitations under the License.
// *
// * SPDX-License-Identifier: Apache-2.0
// */

package net.consensys.linea.zktracer.module.rlptxn;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.corset.CorsetValidator;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.AccessListEntry;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.BlobsWithCommitments;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Quantity;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.VersionedHash;
import org.junit.jupiter.api.Test;

public class RandomTx {
  @Test
public void testRandomTxrcpt() {
  ZkTracer tracer = new ZkTracer();
  Transaction randomTx =
    new Transaction() {
      @Override
      public Hash getHash() {
        return null;
      }

      @Override
      public long getNonce() {
        return 0;
      }

      @Override
      public Optional<? extends Quantity> getGasPrice() {
        return Optional.empty();
      }

      @Override
      public Optional<? extends Quantity> getMaxPriorityFeePerGas() {
        return Transaction.super.getMaxPriorityFeePerGas();
      }

      @Override
      public Optional<? extends Quantity> getMaxFeePerGas() {
        return Transaction.super.getMaxFeePerGas();
      }

      @Override
      public long getGasLimit() {
        return 0;
      }

      @Override
      public Optional<? extends Address> getTo() {
        return Optional.empty();
      }

      @Override
      public Quantity getValue() {
        return null;
      }

      @Override
      public BigInteger getV() {
        return null;
      }

      @Override
      public BigInteger getR() {
        return null;
      }

      @Override
      public BigInteger getS() {
        return null;
      }

      @Override
      public Address getSender() {
        return null;
      }

      @Override
      public Optional<BigInteger> getChainId() {
        return Optional.empty();
      }

      @Override
      public Optional<Bytes> getInit() {
        return Optional.empty();
      }

      @Override
      public Optional<Bytes> getData() {
        return Optional.empty();
      }

      @Override
      public Bytes getPayload() {
        return null;
      }

      @Override
      public TransactionType getType() {
        return null;
      }

      @Override
      public Optional<List<VersionedHash>> getVersionedHashes() {
        return Optional.empty();
      }

      @Override
      public Optional<BlobsWithCommitments> getBlobsWithCommitments() {
        return Optional.empty();
      }

      @Override
      public Optional<Address> contractAddress() {
        return Optional.empty();
      }

      @Override
      public Optional<List<AccessListEntry>> getAccessList() {
        return Optional.empty();
      }

      @Override
      public Bytes encoded() {
        return null;
      }
    };

  // TransactionReceipt randomTxrcpt = new
  // TransactionReceipt(TransactionType.FRONTIER,0,21000,List.of(),Optional.empty());
  tracer.rlpTxn.traceStartTx(randomTx);
  tracer.traceEndConflation();
  assertThat(CorsetValidator.isValid(tracer.getTrace().toJson())).isTrue();
}
}
