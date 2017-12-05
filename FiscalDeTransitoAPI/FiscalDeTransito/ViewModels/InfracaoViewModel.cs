using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace FiscalDeTransitoApi.ViewModels
{
    public class InfracaoViewModel
    {
        public Guid Id { get; set; }
        public String Descricao { get; set; }
        public Int64 IMEI { get; set; }
        public Double Latitude { get; set; }
        public Double Longitude { get; set; }

        public DateTime DataInfracao { get; set; }

        [Required]
        public IFormFile InfracaoFile { get; set; }
    }
}
